package com.qh.pay.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Order
 * @Description: 支付订单参数传递对象
 * @author chyzh
 * @date 2017年10月24日 下午8:20:22
 */
public class Order implements Serializable{
	/**
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 商户号
	 */
	private String merchNo;
	/**
	 * 订单号 由发起支付方提供 在该商户号下保证唯一
	 */
	private String orderNo;
	/***
	 * 渠道编码 微信WAP支付：wap 微信公众号支付：gzh 微信扫码：wx QQ钱包扫码：qq
	 *               支付宝扫码：ali 快捷支付：q 网银支付：wy 代付：acp
	 */
	private String outChannel;
	/***
	 * 标题20个字符以内
	 */
	private String title;
	
	/***
	 * 产品描述 20个字符以内
	 */
	private String product;
	/**
	 * 金额(单位元) 币种 人民币 两位小数
	 */
	private BigDecimal amount;
	/**
	 * 币种 CNY
	 */
	private String currency;
	/***
	 * 订单状态
	 */
	private Integer orderState;
	/***
	 * 前端返回地址
	 */
	private String returnUrl;
	/***
	 * 后台通知地址
	 */
	private String notifyUrl;
	/***
	 * 请求时间 格式 yyyyMMddHHmmss
	 */
	private String reqTime;
	/***
	 * 请求ip
	 */
	private String reqIp;
	/**
	 * 支付用户
	 */
	private String userId;
	/***
	 * 备注
	 */
	private String memo;
	
	//*************************代付
	/**
	 * 持卡用户名
	 */
	private String acctName;
	/***
	 * 
	 * 银行卡号
	 */
	private String bankNo;
	/***
	 * 手机号
	 */
	private String mobile;
	/**
	 * 银行代码
	 */
	private String bankCode;
	/**
	 * 银行名称
	 */
	private String bankName;
	/****
	 * 卡号类型
	 */
	private Integer cardType;
	/**
	 * 账户性质
	 */
	private Integer acctType;
	
	
	//*****************third
	/***支付公司***/
	private String payCompany;
	/***支付商户号***/
	private String payMerch;
	/***第三方支付订单号***/
	private String businessNo;
	/***回调支付金额*/
	private BigDecimal realAmount;
	/***回调提示消息***/
	private String msg;
	/***支付中间结果**/
	private Map<String,String> resultMap = new HashMap<>();
	
	//************持久化数据
	/***创建时间***/
	private int crtDate;
	//成本金额
	private BigDecimal costAmount;
	//启晗代理金额
	private BigDecimal qhAmount;
	//商户代理金额
	private BigDecimal agentAmount;
	
	public String getMerchNo() {
		return merchNo;
	}
	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOutChannel() {
		return outChannel;
	}
	public void setOutChannel(String outChannel) {
		this.outChannel = outChannel;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getOrderState() {
		return orderState;
	}
	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getReqTime() {
		return reqTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}
	public String getReqIp() {
		return reqIp;
	}
	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public Integer getAcctType() {
		return acctType;
	}
	public void setAcctType(Integer acctType) {
		this.acctType = acctType;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAcctName() {
		return acctName;
	}
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Map<String, String> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, String> resultMap) {
		this.resultMap = resultMap;
	}
	public String getPayCompany() {
		return payCompany;
	}
	public void setPayCompany(String payCompany) {
		this.payCompany = payCompany;
	}
	public String getPayMerch() {
		return payMerch;
	}
	public void setPayMerch(String payMerch) {
		this.payMerch = payMerch;
	}
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public BigDecimal getRealAmount() {
		return realAmount;
	}
	/***
	 * 
	 * @Description 元为 单位
	 * @param realAmount
	 */
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(int crtDate) {
		this.crtDate = crtDate;
	}
	public BigDecimal getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}
	public BigDecimal getQhAmount() {
		return qhAmount;
	}
	public void setQhAmount(BigDecimal qhAmount) {
		this.qhAmount = qhAmount;
	}
	public BigDecimal getAgentAmount() {
		return agentAmount;
	}
	public void setAgentAmount(BigDecimal agentAmount) {
		this.agentAmount = agentAmount;
	}
	
	
}
