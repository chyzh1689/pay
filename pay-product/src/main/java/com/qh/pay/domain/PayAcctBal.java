package com.qh.pay.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName PayAcctBal
 * @Description 资金账户余额
 * @author chenyuezhi
 * @Date 2017年11月6日 上午11:22:38
 * @version 1.0.0
 */
public class PayAcctBal  implements Serializable{
	/**
	 * @Field @serialVersionUID : 
	 */
	private static final long serialVersionUID = 1L;
	//用户id
	private Integer userId;
	//用户名
	private String username;
	//用户类型
	private Integer userType;
	//余额
	private BigDecimal balance;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
