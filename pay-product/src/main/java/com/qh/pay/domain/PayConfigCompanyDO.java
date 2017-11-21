package com.qh.pay.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 支付公司配置
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-06 16:00:33
 */
public class PayConfigCompanyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//支付公司
	private String company;
	//支付商户
	private String payMerch;
	//渠道
	private String outChannel;
	//支付成本费率
	private BigDecimal costRate;
	//启晗代理费率
	private BigDecimal qhRate;
	//单笔最大支付额
	private Integer maxPayAmt;
	//单笔最新支付额
	private Integer minPayAmt;
	//创建时间
	private Date crtTime;
	//支付时间段
	private String payPeriod;
	//是否关闭 0 不关闭， 1 关闭
	private Integer ifClose;

	/**
	 * 设置：支付公司
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：支付公司
	 */
	public String getCompany() {
		return company;
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
	 * 设置：渠道
	 */
	public void setOutChannel(String outChannel) {
		this.outChannel = outChannel;
	}
	/**
	 * 获取：渠道
	 */
	public String getOutChannel() {
		return outChannel;
	}
	/**
	 * 设置：支付成本费率
	 */
	public void setCostRate(BigDecimal costRate) {
		this.costRate = costRate;
	}
	/**
	 * 获取：支付成本费率
	 */
	public BigDecimal getCostRate() {
		return costRate;
	}
	/**
	 * 设置：启晗代理费率
	 */
	public void setQhRate(BigDecimal qhRate) {
		this.qhRate = qhRate;
	}
	/**
	 * 获取：启晗代理费率
	 */
	public BigDecimal getQhRate() {
		return qhRate;
	}
	/**
	 * 设置：单笔最大支付额
	 */
	public void setMaxPayAmt(Integer maxPayAmt) {
		this.maxPayAmt = maxPayAmt;
	}
	/**
	 * 获取：单笔最大支付额
	 */
	public Integer getMaxPayAmt() {
		return maxPayAmt;
	}
	/**
	 * 设置：单笔最新支付额
	 */
	public void setMinPayAmt(Integer minPayAmt) {
		this.minPayAmt = minPayAmt;
	}
	/**
	 * 获取：单笔最新支付额
	 */
	public Integer getMinPayAmt() {
		return minPayAmt;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：
	 */
	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}
	/**
	 * 获取：
	 */
	public String getPayPeriod() {
		return payPeriod;
	}
	/**
	 * 设置：是否关闭 0 不关闭， 1 关闭
	 */
	public void setIfClose(Integer ifClose) {
		this.ifClose = ifClose;
	}
	/**
	 * 获取：是否关闭 0 不关闭， 1 关闭
	 */
	public Integer getIfClose() {
		return ifClose;
	}
}
