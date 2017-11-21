package com.qh.pay.domain;

import java.io.Serializable;


/**
 * 商户号下的用户签约信息
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-02 11:21:44
 */
public class MerchUserSignDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//启晗商户
	private String merchNo;
	//商户用户标识
	private String userId;
	//支付公司
	private String payCompany;
	//支付商户
	private String payMerch;
	//快捷签约
	private String sign;
	//其他一些支付信息
	private String info;
	//持卡人姓名
	private String acctName;
	//账户类型
	private String acctType;
	//银行卡号
	private String bankNum;
	//证件类型 1身份证
	private String certType;
	//证件号码
	private String certNo;
	//手机号码
	private String pone;
	//信用卡背面cvv2码后三位
	private String cvv2;
	//有效期，年月，四位数，例：2112
	private String varlidDate;
	//1-快捷支付 2-代扣扣款
	private String collType;

	/**
	 * 设置：启晗商户
	 */
	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}
	/**
	 * 获取：启晗商户
	 */
	public String getMerchNo() {
		return merchNo;
	}
	/**
	 * 设置：商户用户标识
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：商户用户标识
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：支付公司
	 */
	public void setPayCompany(String payCompany) {
		this.payCompany = payCompany;
	}
	/**
	 * 获取：支付公司
	 */
	public String getPayCompany() {
		return payCompany;
	}
	/**
	 * 设置：支付商户
	 */
	public void setPayMerch(String payMerch) {
		this.payMerch = payMerch;
	}
	/**
	 * 获取：支付商户
	 */
	public String getPayMerch() {
		return payMerch;
	}
	/**
	 * 设置：快捷签约
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * 获取：快捷签约
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * 设置：其他一些支付信息
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	/**
	 * 获取：其他一些支付信息
	 */
	public String getInfo() {
		return info;
	}
	/**
	 * 设置：持卡人姓名
	 */
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	/**
	 * 获取：持卡人姓名
	 */
	public String getAcctName() {
		return acctName;
	}
	/**
	 * 设置：账户类型
	 */
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}
	/**
	 * 获取：账户类型
	 */
	public String getAcctType() {
		return acctType;
	}
	/**
	 * 设置：银行卡号
	 */
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	/**
	 * 获取：银行卡号
	 */
	public String getBankNum() {
		return bankNum;
	}
	/**
	 * 设置：证件类型 1身份证
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}
	/**
	 * 获取：证件类型 1身份证
	 */
	public String getCertType() {
		return certType;
	}
	/**
	 * 设置：证件号码
	 */
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	/**
	 * 获取：证件号码
	 */
	public String getCertNo() {
		return certNo;
	}
	/**
	 * 设置：手机号码
	 */
	public void setPone(String pone) {
		this.pone = pone;
	}
	/**
	 * 获取：手机号码
	 */
	public String getPone() {
		return pone;
	}
	/**
	 * 设置：信用卡背面cvv2码后三位
	 */
	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}
	/**
	 * 获取：信用卡背面cvv2码后三位
	 */
	public String getCvv2() {
		return cvv2;
	}
	/**
	 * 设置：有效期，年月，四位数，例：2112
	 */
	public void setVarlidDate(String varlidDate) {
		this.varlidDate = varlidDate;
	}
	/**
	 * 获取：有效期，年月，四位数，例：2112
	 */
	public String getVarlidDate() {
		return varlidDate;
	}
	/**
	 * 设置：1-快捷支付 2-代扣扣款
	 */
	public void setCollType(String collType) {
		this.collType = collType;
	}
	/**
	 * 获取：1-快捷支付 2-代扣扣款
	 */
	public String getCollType() {
		return collType;
	}
}
