package com.qh.pay.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.redisson.api.RLock;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qh.common.config.Constant;
import com.qh.common.utils.R;
import com.qh.pay.api.Order;
import com.qh.pay.api.constenum.AuditType;
import com.qh.pay.api.constenum.FeeType;
import com.qh.pay.api.constenum.OrderParamKey;
import com.qh.pay.api.constenum.OrderState;
import com.qh.pay.api.constenum.OrderType;
import com.qh.pay.api.constenum.ProfitLoss;
import com.qh.pay.api.utils.DateUtil;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.pay.api.utils.QhPayUtil;
import com.qh.pay.api.utils.RSAUtil;
import com.qh.pay.api.utils.RequestUtils;
import com.qh.pay.dao.PayAuditDao;
import com.qh.pay.dao.PayOrderAcpDao;
import com.qh.pay.dao.PayOrderDao;
import com.qh.pay.dao.RecordFoundAcctDao;
import com.qh.pay.dao.RecordMerchBalDao;
import com.qh.pay.domain.Merchant;
import com.qh.pay.domain.PayAcctBal;
import com.qh.pay.domain.PayAuditDO;
import com.qh.pay.domain.PayConfigCompanyDO;
import com.qh.pay.domain.RecordFoundAcctDO;
import com.qh.pay.domain.RecordMerchBalDO;
import com.qh.pay.service.MerchantService;
import com.qh.pay.service.PayConfigCompanyService;
import com.qh.pay.service.PayHandlerService;
import com.qh.pay.service.PayService;
import com.qh.pay.service.third.PayBaseService;
import com.qh.redis.RedisConstants;
import com.qh.redis.service.RedisMsg;
import com.qh.redis.service.RedisUtil;
import com.qh.redis.service.RedissonLockUtil;

/**
 * @ClassName PayServiceImpl
 * @Description 支付实现类
 * @author chenyuezhi
 * @Date 2017年11月6日 下午2:48:20
 * @version 1.0.0
 */
@Service
public class PayServiceImpl implements PayService {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
	@Autowired
	private PayConfigCompanyService payCfgCompService;
	@Autowired
	private PayBaseService payBaseService;
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private PayHandlerService payHandlerService;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private RecordFoundAcctDao rdFoundAcctDao;
	@Autowired
	private RecordMerchBalDao rdMerchBalDao;
	@Autowired
	private PayAuditDao payAuditDao;
	@Autowired
	private PayOrderAcpDao payOrderAcpDao;

	/**
	 * 发起支付
	 */
	@Override
	public Object order(Merchant merchant, JSONObject jo) {
		String merchNo = merchant.getMerchNo();
		String orderNo = jo.getString(OrderParamKey.orderNo.name());
		RLock lock = RedissonLockUtil.getOrderLock(merchNo + RedisConstants.link_symbol + orderNo);
		if (lock.tryLock()) {
			try {
				Order order = new Order();
				// 初始化订单信息
				String initResult = payHandlerService.initOrder(order, jo);
				if (ParamUtil.isNotEmpty(initResult)) {
					logger.error(initResult);
					return R.error(initResult);
				}
				if (RedisUtil.getOrder(merchNo, orderNo) != null) {
					logger.error(merchNo + "," + orderNo + "订单号已经存在！");
					return R.error(merchNo + "," + orderNo + "订单号已经存在！");
				}
				List<Object> payCfgComps = payCfgCompService.getPayCfgCompByOutChannel(order.getOutChannel());
				if (payCfgComps == null || payCfgComps.size() == 0) {
					return R.error(merchNo + "," + order.getOutChannel() + "通道配置错误！");

				}
				// TODO 通过一定的规则获取相应的支付公司通道
				PayConfigCompanyDO payCfgComp = (PayConfigCompanyDO) payCfgComps.get(0);

				order.setPayCompany(payCfgComp.getCompany());
				order.setPayMerch(payCfgComp.getPayMerch());
				R r = (R) payBaseService.order(order);
				if (R.ifSucc(r)) {
					@SuppressWarnings("unchecked")
					Map<String, String> data = (Map<String, String>) r.get(Constant.result_data);
					order.setResultMap(data);
					RedisUtil.setOrder(order);
					return decryptAndSign(data, merchant.getPublicKey()).put(Constant.result_msg,
							r.get(Constant.result_msg));
				}
				return r;
			} finally {
				lock.unlock();
			}
		} else {
			return R.error(merchNo + "," + orderNo + "下单失败！");
		}
	}

	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#notify(java.lang.String,
	 * java.lang.String, java.lang.String,
	 * javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public R notify(String merchNo, String orderNo, HttpServletRequest request, String requestBody) {
		RLock lock = RedissonLockUtil.getOrderLock(merchNo + RedisConstants.link_symbol + orderNo);
		if (lock.tryLock()) {
			try {
				Order order = RedisUtil.getOrder(merchNo, orderNo);
				if (order != null) {
					R r = payBaseService.notify(order, request, requestBody);
					order.setMsg((String) r.get(Constant.result_msg));
					RedisUtil.setOrder(order);
					RedisMsg.orderNotifyMsg(merchNo, orderNo);
					return r;
				}
			} finally {
				lock.unlock();
			}
		}
		return R.error("无效的结果");
	}

	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#orderNotifyMsg(java.lang.String)
	 */
	@Override
	public String orderNotifyMsg(String merchNo, String orderNo) {
		RLock lock = RedissonLockUtil.getOrderLock(merchNo, orderNo);
		if (lock.tryLock()) {
			try {
				Order order = RedisUtil.getOrder(merchNo,orderNo);
				String stateDesc = OrderState.desc().get(order.getOrderState());
				String result = null;
				if (OrderState.init.id() == order.getOrderState()) {
					RequestUtils.doPostJson(order.getNotifyUrl(), R.error(order.getMsg()).jsonStr());
					logger.error("{}状态返回结果：{},{},{}", stateDesc, order.getMerchNo(), order.getOrderNo(), result);
				} else {
					Map<String, String> data = PayService.initRspData(order);
					data.put(OrderParamKey.orderState.name(), String.valueOf(order.getOrderState()));
					data.put(OrderParamKey.businessNo.name(), order.getBusinessNo());
					data.put(OrderParamKey.amount.name(), order.getRealAmount().toString());
					Merchant merchant = merchantService.get(order.getMerchNo());
					result = RequestUtils.doPostJson(order.getNotifyUrl(),
							decryptAndSign(data, merchant.getPublicKey(), order.getMsg()).jsonStr());
					logger.error("{}状态返回结果：{},{},{}", stateDesc, order.getMerchNo(), order.getOrderNo(), result);
				}
				return result;
			} finally {
				lock.unlock();
			}
		}
		return null;
	}

	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#orderDataMsg(java.lang.String,java.lang.String)
	 */
	@Override
	public void orderDataMsg(String merchNo,String orderNo) {
		RLock lock = RedissonLockUtil.getOrderLock(merchNo,orderNo);
		try {
			lock.lock();
			Order order = RedisUtil.getOrder(merchNo,orderNo);
			if (order == null) {
				return;
			}
			try {
				Integer orderState = order.getOrderState();
				if (orderState == OrderState.succ.id() || orderState == OrderState.fail.id()
						|| orderState == OrderState.close.id()) {
					if (this.saveOrderData(order)) {
						RedisUtil.removeOrder(merchNo,orderNo);
					}
				}
			} catch (Exception e) {
				return;
			}
		} finally {
			lock.unlock();
		}

	}

	/**
	 * @Description 保存订单数据
	 * @param order
	 */
	@Transactional
	private boolean saveOrderData(Order order) {
		// 商户信息
		Merchant merchant = merchantService.get(order.getMerchNo());
		// 支付通道信息
		PayConfigCompanyDO payCfgComp = payCfgCompService.get(order.getPayCompany(), order.getPayMerch(),
				order.getOutChannel());
		BigDecimal amount = order.getAmount();
		// 成本金额
		order.setCostAmount(ParamUtil.multBig(amount, payCfgComp.getCostRate()));
		// 启晗代理金额
		order.setQhAmount(ParamUtil.multBig(amount, payCfgComp.getQhRate()));
		// 商户代理金额
		BigDecimal feeRate = null;
		if (ParamUtil.isNotEmpty(merchant.getFeeRate())) {
			feeRate = JSON.parseObject(merchant.getFeeRate()).getBigDecimal(order.getOutChannel());
		}
		if (feeRate != null) {
			order.setAgentAmount(ParamUtil.multSmall(amount, payCfgComp.getQhRate()));
		} else {
			order.setAgentAmount(BigDecimal.ZERO);
		}
		int crtDate = DateUtil.getCurrentTimeInt();
		order.setCrtDate(crtDate);
		if (ParamUtil.isNotEmpty(order.getMsg()) && order.getMsg().length() > 50) {
			order.setMsg(order.getMsg().substring(0, 50));
		}
		payOrderDao.save(order);
		int orderState = order.getOrderState();
		if (orderState != OrderState.succ.id()) {
			return true;
		}
		// 增加商户余额以及流水
		RecordMerchBalDO rdMerchBal = payHandlerService.balForMerchAdd(order, order.getAmount().subtract(order.getQhAmount()),
				FeeType.merchIn.id(),  OrderType.pay.id());
		rdMerchBal.setCrtDate(crtDate);
		rdMerchBalDao.save(rdMerchBal);

		// 增加平台资金账户余额以及流水
		RecordFoundAcctDO rdFoundAcct = payHandlerService.balForPlatAdd(order,order.getQhAmount().subtract(order.getCostAmount()).subtract(order.getAgentAmount()), 
				FeeType.platIn.id(),OrderType.pay.id());
		rdFoundAcct.setCrtDate(crtDate);
		rdFoundAcctDao.save(rdFoundAcct);

		// 增加商户代理余额以及流水
		if (order.getAgentAmount().compareTo(BigDecimal.ZERO) != 0) {
			rdFoundAcct = payHandlerService.balForAgentAdd(order, order.getAgentAmount(),merchant.getAgentUser(), FeeType.agentIn.id(),OrderType.pay.id());
			rdFoundAcct.setCrtDate(crtDate);
			rdFoundAcctDao.save(rdFoundAcct);
		}
		return true;
	}

	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#query(com.qh.pay.domain.Merchant,
	 * com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public R query(Merchant merchant, JSONObject jo) {
		String orderNo = jo.getString(OrderParamKey.orderNo.name());
		if (ParamUtil.isEmpty(orderNo)) {
			return R.error("查询支付订单号为空！");
		}
		RLock lock = RedissonLockUtil.getOrderLock(merchant.getMerchNo() + RedisConstants.link_symbol + orderNo);
		if (lock.tryLock()) {
			Order order = null;
			Integer orderState = null;
			String msg = null;
			try {
				order = RedisUtil.getOrder(merchant.getMerchNo(), orderNo);
				if (order == null) {
					order = payOrderDao.get(orderNo, merchant.getMerchNo());
				}
				if (order == null) {
					return R.error(merchant.getMerchNo() + "," + orderNo + "支付订单不存在！");
				}
				orderState = order.getOrderState();
				// 无支付结果去第三方查询
				if (OrderState.init.id() == orderState) {
					R r = payBaseService.query(order);
					if (R.ifError(r)) {
						return r;
					}
					RedisUtil.setOrder(order);
					msg = (String) r.get(Constant.result_msg);
				}
			} finally {
				lock.unlock();
			}
			Map<String, String> data = PayService.initRspData(order);
			data.put(OrderParamKey.orderState.name(), String.valueOf(orderState));
			data.put(OrderParamKey.businessNo.name(), order.getBusinessNo());
			data.put(OrderParamKey.amount.name(), order.getRealAmount().toString());
			return decryptAndSign(data, merchant.getPublicKey(), msg);
		} else {
			return R.error("查询过于繁忙，请稍后再试！");
		}
	}

	/**
	 * @Description 公钥加密，私钥签名
	 * @param data
	 * @return
	 */
	private R decryptAndSign(Map<String, String> data, String publicKey) {
		try {
			byte[] context = RSAUtil.encryptByPublicKey(JSON.toJSONBytes(data), publicKey);
			String sign = RSAUtil.sign(context, QhPayUtil.getQhPrivateKey());
			return R.ok().put("sign", sign).put("context", context);
		} catch (Exception e) {
			logger.error("返回数据 公钥加密，私钥签名 失败！");
		}
		return R.error("返回数据 公钥加密，私钥签名 失败！");
	}

	/**
	 * @Description 公钥加密，私钥签名
	 * @param data
	 * @return
	 */
	private R decryptAndSign(Map<String, String> data, String publicKey, String msg) {
		try {
			byte[] context = RSAUtil.encryptByPublicKey(JSON.toJSONBytes(data), publicKey);
			String sign = RSAUtil.sign(context, QhPayUtil.getQhPrivateKey());
			return R.ok().put("sign", sign).put("context", context).put(Constant.result_msg, msg);
		} catch (Exception e) {
			logger.error("返回数据 公钥加密，私钥签名 失败！");
		}
		return R.error("返回数据 公钥加密，私钥签名 失败！");
	}

	

	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#orderAcp(com.qh.pay.domain.Merchant,
	 * com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public R orderAcp(Merchant merchant, JSONObject jo) {
		String merchNo = merchant.getMerchNo();
		String orderNo = jo.getString(OrderParamKey.orderNo.name());
		RLock lock = RedissonLockUtil.getOrderAcpLock(merchNo + RedisConstants.link_symbol + orderNo);
		if (lock.tryLock()) {
			try {
				Order order = new Order();
				// 初始化订单信息
				String initResult = payHandlerService.initOrder(order, jo);

				if (ParamUtil.isNotEmpty(initResult)) {
					logger.error(initResult);
					return R.error(initResult);
				}
				if (RedisUtil.getOrderAcp(merchNo, orderNo) != null) {
					logger.error(merchNo + "," + orderNo + "订单号已经存在！");
					return R.error(merchNo + "," + orderNo + "订单号已经存在！");
				}
				List<Object> payCfgComps = payCfgCompService.getPayCfgCompByOutChannel(order.getOutChannel());
				if (payCfgComps == null || payCfgComps.size() == 0) {
					return R.error(merchNo + "," + order.getOutChannel() + "通道配置错误！");
				}
				// TODO 通过一定的规则获取相应的支付公司通道
				PayConfigCompanyDO payCfgComp = (PayConfigCompanyDO) payCfgComps.get(0);
				order.setPayCompany(payCfgComp.getCompany());
				order.setPayMerch(payCfgComp.getPayMerch());

				RecordMerchBalDO rdMerchBal = PayService.initRdMerchBal(order, FeeType.merchAcpOut.id(),
						OrderType.acp.id(), ProfitLoss.loss.id());
				// 检查商户余额
				RLock merchLock = RedissonLockUtil.getBalMerchLock(merchNo);
				try {
					merchLock.lock();
					PayAcctBal pab = RedisUtil.getMerchBal(merchNo);
					if (pab == null || pab.getBalance().compareTo(order.getAmount()) < 0) {
						return R.error("商户号 " + merchNo + ",余额不足！");
					}
					rdMerchBal.setBeforeAmt(pab.getBalance());
					rdMerchBal.setTranAmt(order.getAmount());
					// 先扣减余额
					pab.setBalance(pab.getBalance().subtract(order.getAmount()));
					rdMerchBal.setAfterAmt(pab.getBalance());
					RedisUtil.setMerchBal(pab);
				} finally {
					merchLock.unlock();
				}

				PayAuditDO payAudit = PayService.initPayAudit(order, AuditType.order_acp.id());
				int count = 0;
				try {
					payAudit.setCrtTime(rdMerchBal.getCrtDate());
					count = payAuditDao.save(payAudit);
				} catch (Exception e) {
					count = 0;
				}
				if (count == 1) {
					rdMerchBal.setCrtDate(DateUtil.getCurrentTimeInt());
					try {
						rdMerchBalDao.save(rdMerchBal);
					} catch (Exception e) {
					}
					RedisUtil.setOrderAcp(order);
					Map<String, String> data = PayService.initRspData(order);
					return decryptAndSign(data, merchant.getPublicKey(), "代付订单已受理!");
				} else {// 出错了返还
					rdMerchBal = payHandlerService.balForMerchAdd(order, order.getAmount(), FeeType.merchAcpFail.id(), OrderType.acp.id());
					rdMerchBal.setCrtDate(payAudit.getCrtTime());
					rdMerchBalDao.save(rdMerchBal);
					return R.error("代付异常！");
				}
			} finally {
				lock.unlock();
			}
		} else {
			return R.error(merchNo + "," + orderNo + "下单失败！");
		}
	}

	/*
	 * (非 Javadoc) Description: 审核未通过 余额返还
	 * 
	 * @see
	 * com.qh.pay.service.PayService#orderAcpNopassDataMsg(java.lang.String,java.lang.String)
	 */
	@Override
	public void orderAcpNopassDataMsg(String merchNo,String orderNo) {
		RLock lock = RedissonLockUtil.getOrderAcpLock(merchNo,orderNo);
		if (lock.tryLock()) {
			try {
				Order order = RedisUtil.getOrderAcp(merchNo,orderNo);
				if (order != null) {
					RecordMerchBalDO rdMerchBalDO = payHandlerService.balForMerchAdd(order, order.getAmount(),FeeType.merchAcpNopass.id(),OrderType.acp.id());
					RedisUtil.removeOrderAcp(merchNo,orderNo);
					rdMerchBalDao.save(rdMerchBalDO);
					RequestUtils.doPostJson(order.getNotifyUrl(), R.error(order.getMsg()).jsonStr());
				}
			} finally {
				lock.unlock();
			}
		}
	}

	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#orderAcp(java.lang.String,java.lang.String)
	 */
	@Override
	public R orderAcp(String merchNo,String orderNo) {
		RLock lock = RedissonLockUtil.getOrderAcpLock(merchNo,orderNo);
		if (lock.tryLock()) {
			try {
				Order order = RedisUtil.getOrderAcp(merchNo,orderNo);
				R r = payBaseService.orderAcp(order);
				order.setMsg((String) r.get(Constant.result_msg));
				RedisUtil.setOrder(order);
				return r;
			} finally {
				lock.unlock();
			}
		}
		return null;
	}

	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#orderAcpNotifyMsg(java.lang.String,java.lang.String)
	 */
	@Override
	public String orderAcpNotifyMsg(String merchNo,String orderNo) {
		RLock lock = RedissonLockUtil.getOrderAcpLock(merchNo,orderNo);
		if (lock.tryLock()) {
			try {
				Order order = RedisUtil.getOrder(merchNo,orderNo);
				String stateDesc = OrderState.desc().get(order.getOrderState());
				String result = null;
				if (OrderState.init.id() == order.getOrderState()) {
					result = RequestUtils.doPostJson(order.getNotifyUrl(), R.error(order.getMsg()).jsonStr());
					logger.error("代付异常发起{}状态返回结果：{},{},{}", stateDesc, order.getMerchNo(), order.getOrderNo());
				} else {
					Map<String, String> data = PayService.initRspData(order);
					data.put(OrderParamKey.orderState.name(), String.valueOf(order.getOrderState()));
					data.put(OrderParamKey.businessNo.name(), order.getBusinessNo());
					data.put(OrderParamKey.amount.name(), order.getRealAmount().toString());
					Merchant merchant = merchantService.get(order.getMerchNo());
					result = RequestUtils.doPostJson(order.getNotifyUrl(),
							decryptAndSign(data, merchant.getPublicKey(), order.getMsg()).jsonStr());
					logger.error("{}状态返回结果：{},{},{}", stateDesc, order.getMerchNo(), order.getOrderNo(), result);
				}
				return result;
			} finally {
				lock.unlock();
			}
		}
		return null;
	}

	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#orderAcpDataMsg(java.lang.String,java.lang.String)
	 */
	@Override
	public void orderAcpDataMsg(String merchNo,String orderNo) {
		RLock lock = RedissonLockUtil.getOrderAcpLock(merchNo,orderNo);
		if (lock.tryLock()) {
			try {
				Order order = RedisUtil.getOrderAcp(merchNo,orderNo);
				if (order != null) {
					this.saveOrderAcpData(order);
					RedisUtil.removeOrderAcp(merchNo,orderNo);
					RequestUtils.doPostJson(order.getNotifyUrl(), R.error(order.getMsg()).jsonStr());
				}
			} finally {
				lock.unlock();
			}
		}

	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.PayService#notifyAcp(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public R notifyAcp(String merchNo, String orderNo, HttpServletRequest request, String requestBody) {
		RLock lock = RedissonLockUtil.getOrderAcpLock(merchNo + RedisConstants.link_symbol + orderNo);
		if (lock.tryLock()) {
			try {
				Order order = RedisUtil.getOrderAcp(merchNo, orderNo);
				if (order != null) {
					R r = payBaseService.notifyAcp(order, request, requestBody);
					order.setMsg((String) r.get(Constant.result_msg));
					RedisUtil.setOrder(order);
					RedisMsg.orderNotifyMsg(merchNo, orderNo);
					return r;
				}
			} finally {
				lock.unlock();
			}
		}
		return R.error("无效的结果");
	}

	
	/*
	 * (非 Javadoc) Description:
	 * 
	 * @see com.qh.pay.service.PayService#acpQuery(com.qh.pay.domain.Merchant,
	 * com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public R acpQuery(Merchant merchant, JSONObject jo) {
		String orderNo = jo.getString(OrderParamKey.orderNo.name());
		if (ParamUtil.isEmpty(orderNo)) {
			return R.error("查询支付订单号为空！");
		}
		RLock lock = RedissonLockUtil.getOrderAcpLock(merchant.getMerchNo() + RedisConstants.link_symbol + orderNo);
		if (lock.tryLock()) {
			Order order = null;
			Integer orderState = null;
			String msg = null;
			try {
				order = RedisUtil.getOrderAcp(merchant.getMerchNo(), orderNo);
				if (order == null) {
					order = payOrderAcpDao.get(orderNo, merchant.getMerchNo());
				}
				if (order == null) {
					return R.error(merchant.getMerchNo() + "," + orderNo + "支付订单不存在！");
				}
				orderState = order.getOrderState();
				// 无支付结果去第三方查询
				if (OrderState.init.id() == orderState) {
					R r = payBaseService.acpQuery(order);
					if (R.ifError(r)) {
						return r;
					}
					RedisUtil.setOrder(order);
					msg = (String) r.get(Constant.result_msg);
				}
			} finally {
				lock.unlock();
			}
			Map<String, String> data = PayService.initRspData(order);
			data.put(OrderParamKey.orderState.name(), String.valueOf(orderState));
			data.put(OrderParamKey.businessNo.name(), order.getBusinessNo());
			data.put(OrderParamKey.amount.name(), order.getRealAmount().toString());
			return decryptAndSign(data, merchant.getPublicKey(), msg);
		} else {
			return R.error("查询过于繁忙，请稍后再试！");
		}
	}

	/**
	 * @Description 保存代付订单数据
	 * @param order
	 */
	private boolean saveOrderAcpData(Order order) {
		Integer orderState = order.getOrderState();
		if(OrderState.succ.id() == orderState){
			// 商户信息
			Merchant merchant = merchantService.get(order.getMerchNo());
			// 支付通道信息
			PayConfigCompanyDO payCfgComp = payCfgCompService.get(order.getPayCompany(), order.getPayMerch(),
					order.getOutChannel());
			BigDecimal amount = order.getAmount();
			// 成本金额
			order.setCostAmount(ParamUtil.multBig(amount, payCfgComp.getCostRate()));
			// 启晗代理金额
			order.setQhAmount(ParamUtil.multBig(amount, payCfgComp.getQhRate()));
			// 商户代理金额
			BigDecimal feeRate = null;
			if (ParamUtil.isNotEmpty(merchant.getFeeRate())) {
				feeRate = JSON.parseObject(merchant.getFeeRate()).getBigDecimal(order.getOutChannel());
			}
			if (feeRate != null) {
				order.setAgentAmount(ParamUtil.multSmall(amount, payCfgComp.getQhRate()));
			} else {
				order.setAgentAmount(BigDecimal.ZERO);
			}
			int crtDate = DateUtil.getCurrentTimeInt();
			order.setCrtDate(crtDate);
			if (ParamUtil.isNotEmpty(order.getMsg()) && order.getMsg().length() > 50) {
				order.setMsg(order.getMsg().substring(0, 50));
			}
			payOrderDao.save(order);
			// 增加平台资金账户余额以及流水
			RecordFoundAcctDO rdFoundAcct = payHandlerService.balForPlatAdd(order,order.getQhAmount().subtract(order.getCostAmount()).subtract(order.getAgentAmount()),
					FeeType.platAcpIn.id(),OrderType.acp.id());
			rdFoundAcct.setCrtDate(crtDate);
			rdFoundAcctDao.save(rdFoundAcct);

			// 增加商户代理余额以及流水
			if (order.getAgentAmount().compareTo(BigDecimal.ZERO) != 0) {
				rdFoundAcct = payHandlerService.balForAgentAdd(order,order.getAgentAmount(), merchant.getAgentUser(), FeeType.agentAcpIn.id(),OrderType.acp.id());
				rdFoundAcct.setCrtDate(crtDate);
				rdFoundAcctDao.save(rdFoundAcct);
			}
			return true;
		}
		return false;
	}
}
