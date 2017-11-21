package com.qh.pay.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.qh.common.config.CfgKeyConst;
import com.qh.common.utils.R;
import com.qh.pay.api.Order;
import com.qh.pay.api.constenum.AuditResult;
import com.qh.pay.api.constenum.OrderParamKey;
import com.qh.pay.api.utils.DateUtil;
import com.qh.pay.domain.Merchant;
import com.qh.pay.domain.PayAuditDO;
import com.qh.pay.domain.RecordFoundAcctDO;
import com.qh.pay.domain.RecordMerchBalDO;
import com.qh.redis.service.RedisUtil;

/**
 * @ClassName PayService
 * @Description 支付服务类
 * @author chenyuezhi
 * @Date 2017年11月6日 下午2:44:55
 * @version 1.0.0
 */
public interface PayService {

	/**
	 * @Description 发起支付
	 * @param merchant
	 * @param resultMap
	 * @return 
	 */
	Object order(Merchant merchant, JSONObject jo);
	
	/**
	 * @Description 支付后台回调
	 * @param merchNo
	 * @param orderNo
	 * @param request
	 * @param requestBody
	 */
	R notify(String merchNo, String orderNo, HttpServletRequest request, String requestBody);

	
	/**
	 * @Description 订单回调后通知
	 * @param merchNo
	 * @param orderNo
	 */
	String orderNotifyMsg(String merchNo, String orderNo);
	
	/**
	 * @Description 订单通知保存
	 * @param merchNo
	 * @param orderNo 
	 */
	void orderDataMsg(String merchNo, String orderNo);

	/**
	 * @Description 支付订单查询
	 * @param merchant
	 * @param jo
	 * @return
	 */
	R query(Merchant merchant, JSONObject jo);
	
	/**
	 * @Description 代付受理
	 * @param merchant
	 * @param jo
	 * @return
	 */
	R orderAcp(Merchant merchant, JSONObject jo);
	
	
	/**
	 * @Description 审核未通过
	 * @param merchNo
	 * @param orderNo 
	 */
	void orderAcpNopassDataMsg(String merchNo, String orderNo);
	/**
	 * @Description 代付下单发起
	 * @param merchNo
	 * @param orderNo 
	 */
	R orderAcp(String merchNo, String orderNo);
	
	/**
	 * @Description 代付订单回调通知
	 * @param merchNo
	 * @param orderNo 
	 * @return
	 */
	String orderAcpNotifyMsg(String merchNo, String orderNo);
	
	/**
	 * @Description 代付最终保存结果
	 * @param merchNo
	 * @param orderNo 
	 */
	void orderAcpDataMsg(String merchNo, String orderNo);
	
	/**
	 * @Description 代付回调通知
	 * @param merchNo
	 * @param orderNo
	 * @param request
	 * @param requestBody
	 * @return
	 */
	R notifyAcp(String merchNo, String orderNo, HttpServletRequest request, String requestBody);
	
	/**
	 * @Description 代付查询
	 * @param merchant
	 * @param jo
	 * @return
	 */
	R acpQuery(Merchant merchant, JSONObject jo);
	
	/**
	 * 
	 * @Description 前台返回地址
	 * @param order
	 * @return
	 */
	public static String commonReturnUrl(Order order){
		return RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain) + RedisUtil.getSysConfigValue(CfgKeyConst.pay_return_url) + 
				order.getPayCompany() + "/" + order.getMerchNo() + "/" + order.getOrderNo();
	}
	
	/**
	 * 
	 * @Description 后台返回地址
	 * @param order
	 * @return
	 */
	public static String commonNotifyUrl(Order order){
		return RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain) + RedisUtil.getSysConfigValue(CfgKeyConst.pay_notify_url) +  
				order.getPayCompany() + "/" + order.getMerchNo() + "/" + order.getOrderNo();
	}
	
	/**
	 * 
	 * @Description 前台返回地址
	 * @param order
	 * @return
	 */
	public static String commonAcpReturnUrl(Order order){
		return RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain)  + RedisUtil.getSysConfigValue(CfgKeyConst.pay_acp_return_url) + 
				order.getPayCompany() + "/" + order.getMerchNo() + "/" + order.getOrderNo();
	}
	
	/**
	 * 
	 * @Description 后台返回地址
	 * @param order
	 * @return
	 */
	public static String commonAcpNotifyUrl(Order order){
		return RedisUtil.getSysConfigValue(CfgKeyConst.pay_domain) + RedisUtil.getSysConfigValue(CfgKeyConst.pay_acp_notify_url) +  
				order.getPayCompany() + "/" + order.getMerchNo() + "/" + order.getOrderNo();
	}
	/**
	 * 
	 * @Description 初始化返回数据
	 * @param order
	 * @return
	 */
	public static Map<String,String> initRspData(Order order){
		Map<String, String> data = new HashMap<>();
		data.put(OrderParamKey.merchNo.name(), order.getMerchNo());
		data.put(OrderParamKey.orderNo.name(), order.getOrderNo());
		data.put(OrderParamKey.outChannel.name(), order.getOutChannel());
		return data;
	}

	/**
	 * @Description 初始化商户资金流水
	 * @param order
	 * @return
	 */
	public static RecordMerchBalDO initRdMerchBal(Order order,int feeType,int orderType,int profitLoss) {
		RecordMerchBalDO rdMerchBal = new RecordMerchBalDO();
		rdMerchBal.setOrderNo(order.getOrderNo());
		rdMerchBal.setMerchNo(order.getMerchNo());
		rdMerchBal.setFeeType(feeType);
		rdMerchBal.setOrderType(orderType);
		rdMerchBal.setCrtDate(order.getCrtDate());
		rdMerchBal.setProfitLoss(profitLoss);
		return rdMerchBal;
	}

	/**
	 * @Description 初始化平台资金流水
	 * @param order
	 * @return
	 */
	public static RecordFoundAcctDO initRdFoundAcct(Order order,int feeType,int orderType,int profitLoss,String username) {
		RecordFoundAcctDO rdFoundAcct = new RecordFoundAcctDO();
		rdFoundAcct.setOrderNo(order.getOrderNo());
		rdFoundAcct.setMerchNo(order.getMerchNo());
		rdFoundAcct.setFeeType(feeType);
		rdFoundAcct.setOrderType(orderType);
		rdFoundAcct.setCrtDate(order.getCrtDate());
		rdFoundAcct.setProfitLoss(profitLoss);
		rdFoundAcct.setUsername(username);
		return rdFoundAcct;
	}
	/**
	 * @Description 初始化平台资金流水
	 * @param order
	 * @return
	 */
	public static RecordFoundAcctDO initRdFoundAcct(Order order,int feeType,int orderType,int profitLoss) {
		return initRdFoundAcct(order, feeType, orderType, profitLoss, null);
	}

	/**
	 * @Description 初始化审核记录
	 * @param order
	 * @return
	 */
	public static PayAuditDO initPayAudit(Order order,int auditType) {
		PayAuditDO payAudit = new PayAuditDO();
		payAudit.setOrderNo(order.getOrderNo());
		payAudit.setMerchNo(order.getMerchNo());
		payAudit.setAuditType(auditType);
		payAudit.setAuditResult(AuditResult.init.id());
		payAudit.setCrtTime(DateUtil.getCurrentTimeInt());
		return payAudit;
	}


}
