package com.qh.pay.service.impl;

import java.math.BigDecimal;

import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qh.pay.api.Order;
import com.qh.pay.api.constenum.AcctType;
import com.qh.pay.api.constenum.BankCode;
import com.qh.pay.api.constenum.CardType;
import com.qh.pay.api.constenum.Currency;
import com.qh.pay.api.constenum.OrderParamKey;
import com.qh.pay.api.constenum.OrderState;
import com.qh.pay.api.constenum.OutChannel;
import com.qh.pay.api.constenum.ProfitLoss;
import com.qh.pay.api.utils.DateUtil;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.pay.domain.PayAcctBal;
import com.qh.pay.domain.RecordFoundAcctDO;
import com.qh.pay.domain.RecordMerchBalDO;
import com.qh.pay.service.PayHandlerService;
import com.qh.pay.service.PayService;
import com.qh.redis.service.RedisUtil;
import com.qh.redis.service.RedissonLockUtil;

/**
 * 
 * @ClassName PayHandlerServiceImpl
 * @Description 支付处理类
 * @author chenyuezhi
 * @Date 2017年11月20日 下午7:20:30
 * @version 1.0.0
 */
@Service
public class PayHandlerServiceImpl implements PayHandlerService{
	
	
	/**
	 * 
	 * @Description 初始化支付订单
	 * @param resultMap
	 * @return
	 */
	@Override
	public String initOrder(Order order, JSONObject jo) {
		// 订单状态
		order.setOrderState(OrderState.init.id());
		// 商户号
		order.setMerchNo(jo.getString(OrderParamKey.merchNo.name()));
		// 订单号
		order.setOrderNo(jo.getString(OrderParamKey.orderNo.name()));
		if (ParamUtil.isEmpty(order)) {
			return "订单号为空";
		}
		// 渠道编码
		order.setOutChannel(jo.getString(OrderParamKey.outChannel.name()));
		if (ParamUtil.isEmpty(order.getOutChannel()) || !OutChannel.desc().containsKey(order.getOutChannel())) {
			return "渠道编码为空或不正确";
		}
		// 如果是网银支付 或者代付
		if (OutChannel.wy.name().equals(order.getOutChannel()) || OutChannel.acp.name().equals(order.getOutChannel())) {
			order.setBankName(jo.getString(OrderParamKey.bankName.name()));
			if (ParamUtil.isEmpty(order.getBankName()) || !BankCode.desc().containsKey(order.getBankName())) {
				return "银行名称为空或不正确";
			}
			
			String cardType = jo.getString(OrderParamKey.cardType.name());
			if (ParamUtil.isEmpty(cardType)) {
				order.setCardType(CardType.savings.id());
			}else if(!cardType.matches("\\d")){
				return "银行卡类型不正确";
			}else{
				order.setCardType(Integer.parseInt(cardType));
				if(!CardType.desc().containsKey(order.getCardType())){
					return "银行卡类型值不正确";
				}
			}
			
			String acctType = jo.getString(OrderParamKey.acctType.name());
			if (ParamUtil.isEmpty(acctType)) {
				order.setAcctType(AcctType.pri.id());
			}else if(!acctType.matches("\\d")){
				return "账户类型不正确";
			}else{
				order.setAcctType(Integer.parseInt(acctType));
				if(!AcctType.desc().containsKey(order.getAcctType())){
					return "账户类型值不正确";
				}
			}
		}
		// 标题
		order.setTitle(jo.getString(OrderParamKey.title.name()));
		// 产品名称
		order.setProduct(jo.getString(OrderParamKey.product.name()));
		if (ParamUtil.isEmpty(order.getProduct())) {
			return "产品名称为空或不正确！";
		}
		// 金额
		String amount = jo.getString(OrderParamKey.amount.name());
		if (ParamUtil.isNotEmpty(amount)) {
			order.setAmount(new BigDecimal(amount));
		} else {
			return "订单金额为空！";
		}
		if (order.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			return "订单金额数异常！";
		}
		// 币种
		order.setCurrency(Currency.CNY.name());
		// 前台返回地址
		order.setReturnUrl(jo.getString(OrderParamKey.returnUrl.name()));
		// 后台通知地址
		order.setNotifyUrl(jo.getString(OrderParamKey.notifyUrl.name()));
		// 请求时间
		order.setReqTime(jo.getString(OrderParamKey.reqTime.name()));
		if (ParamUtil.isEmpty(order.getReqTime())) {
			return "请求时间为空！";
		}
		// 商户号 用户唯一标准
		order.setUserId(jo.getString(OrderParamKey.userId.name()));
		// 备注
		order.setMemo(jo.getString(OrderParamKey.memo.name()));

		return null;
	}
	
	/**
	 * 
	 * @Description 商户余额变更  减少
	 * @param order
	 * @param amount
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	@Override
	public RecordMerchBalDO balForMerchSub(Order order, BigDecimal amount, int feeType, int orderType) {
		RecordMerchBalDO rdMerchBal = PayService.initRdMerchBal(order, feeType, orderType, ProfitLoss.loss.id());
		rdMerchBal.setCrtDate(DateUtil.getCurrentTimeInt());
		rdMerchBal.setTranAmt(amount);
		RLock merchLock = RedissonLockUtil.getBalMerchLock(order.getMerchNo());
		try {
			merchLock.lock();
			PayAcctBal pab = RedisUtil.getMerchBal(order.getMerchNo());
			rdMerchBal.setBeforeAmt(pab.getBalance());
			// 返还余额
			pab.setBalance(pab.getBalance().subtract(amount));
			rdMerchBal.setAfterAmt(pab.getBalance());
			RedisUtil.setMerchBal(pab);
		} finally {
			merchLock.unlock();
		}
		return rdMerchBal;
	}
	/**
	 * 
	 * @Description 商户余额变更 增加
	 * @param order
	 * @param amount
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	@Override
	public RecordMerchBalDO balForMerchAdd(Order order, BigDecimal amount, int feeType, int orderType) {
		RecordMerchBalDO rdMerchBal = PayService.initRdMerchBal(order, feeType, orderType, ProfitLoss.profit.id());
		rdMerchBal.setCrtDate(DateUtil.getCurrentTimeInt());
		rdMerchBal.setTranAmt(amount);
		RLock merchLock = RedissonLockUtil.getBalMerchLock(order.getMerchNo());
		try {
			merchLock.lock();
			PayAcctBal pab = RedisUtil.getMerchBal(order.getMerchNo());
			rdMerchBal.setBeforeAmt(pab.getBalance());
			pab.setBalance(pab.getBalance().add(rdMerchBal.getTranAmt()));
			rdMerchBal.setAfterAmt(pab.getBalance());
			RedisUtil.setMerchBal(pab);
		} finally {
			merchLock.unlock();
		}
		return rdMerchBal;
	}

	
	/**
	 * 
	 * @Description 处理代理资金流水 减少
	 * @param order
	 * @param amount
	 * @param agentUser
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	@Override
	public RecordFoundAcctDO balForAgentSub(Order order,BigDecimal amount, String agentUser, int feeType, int orderType) {
		RecordFoundAcctDO rdFoundAcct = PayService.initRdFoundAcct(order, feeType, orderType, ProfitLoss.loss.id());
		rdFoundAcct.setCrtDate(DateUtil.getCurrentTimeInt());
		rdFoundAcct.setTranAmt(amount);
		rdFoundAcct.setUsername(agentUser);
		RLock lock = RedissonLockUtil.getBalFoundAcctLock(agentUser);
		try {
			lock.lock();
			PayAcctBal payAcctBal = RedisUtil.getAgentBal(agentUser);
			rdFoundAcct.setBeforeAmt(payAcctBal.getBalance());
			payAcctBal.setBalance(payAcctBal.getBalance().subtract(rdFoundAcct.getTranAmt()));
			rdFoundAcct.setAfterAmt(payAcctBal.getBalance());
			RedisUtil.setPayFoundBal(payAcctBal);
		} finally {
			lock.unlock();
		}
		return rdFoundAcct;
	}
	
	
	/**
	 * 
	 * @Description 处理代理资金流水 增加
	 * @param order
	 * @param amount
	 * @param agentUser
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	@Override
	public RecordFoundAcctDO balForAgentAdd(Order order, BigDecimal amount,String agentUser,int feeType, int orderType) {
		RecordFoundAcctDO rdFoundAcct = PayService.initRdFoundAcct(order, feeType, orderType, ProfitLoss.profit.id());
		rdFoundAcct.setCrtDate(DateUtil.getCurrentTimeInt());
		rdFoundAcct.setTranAmt(amount);
		rdFoundAcct.setUsername(agentUser);
		RLock lock = RedissonLockUtil.getBalFoundAcctLock(agentUser);
		try {
			lock.lock();
			PayAcctBal payAcctBal = RedisUtil.getAgentBal(agentUser);
			rdFoundAcct.setBeforeAmt(payAcctBal.getBalance());
			payAcctBal.setBalance(payAcctBal.getBalance().add(rdFoundAcct.getTranAmt()));
			rdFoundAcct.setAfterAmt(payAcctBal.getBalance());
			RedisUtil.setPayFoundBal(payAcctBal);
		} finally {
			lock.unlock();
		}
		return rdFoundAcct;
	}
	
	/**
	 * 
	 * @Description 处理平台资金流水 减少
	 * @param order
	 * @param amount
	 * @param feeType
	 * @param orderType
	 * @return
	 */
	@Override
	public RecordFoundAcctDO balForPlatSub(Order order, BigDecimal amount, int feeType,int orderType) {
		RecordFoundAcctDO rdFoundAcct = PayService.initRdFoundAcct(order, feeType, orderType, ProfitLoss.loss.id());
		rdFoundAcct.setCrtDate(DateUtil.getCurrentTimeInt());
		rdFoundAcct.setTranAmt(amount);
		RLock lock = RedissonLockUtil.getBalFoundAcctLock();
		try {
			lock.lock();
			PayAcctBal payAcctBal = RedisUtil.getPayFoundBal();
			rdFoundAcct.setUsername(payAcctBal.getUsername());
			rdFoundAcct.setBeforeAmt(payAcctBal.getBalance());
			payAcctBal.setBalance(payAcctBal.getBalance().subtract(rdFoundAcct.getTranAmt()));
			rdFoundAcct.setAfterAmt(payAcctBal.getBalance());
			RedisUtil.setPayFoundBal(payAcctBal);
		} finally {
			lock.unlock();
		}
		return rdFoundAcct;
	}
	
	/**
	 * @Description 处理平台资金流水 增加
	 * @param order
	 * @return
	 */
	@Override
	public RecordFoundAcctDO balForPlatAdd(Order order, BigDecimal amount, int feeType, int orderType) {
		RecordFoundAcctDO rdFoundAcct = PayService.initRdFoundAcct(order, feeType, orderType, ProfitLoss.profit.id());
		rdFoundAcct.setCrtDate(DateUtil.getCurrentTimeInt());
		rdFoundAcct.setTranAmt(amount);
		RLock lock = RedissonLockUtil.getBalFoundAcctLock();
		try {
			lock.lock();
			PayAcctBal payAcctBal = RedisUtil.getPayFoundBal();
			rdFoundAcct.setUsername(payAcctBal.getUsername());
			rdFoundAcct.setBeforeAmt(payAcctBal.getBalance());
			payAcctBal.setBalance(payAcctBal.getBalance().add(rdFoundAcct.getTranAmt()));
			rdFoundAcct.setAfterAmt(payAcctBal.getBalance());
			RedisUtil.setPayFoundBal(payAcctBal);
		} finally {
			lock.unlock();
		}
		return rdFoundAcct;
	}

}
