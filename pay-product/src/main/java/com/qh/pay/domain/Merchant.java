package com.qh.pay.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity(name = "merchant")
public class Merchant implements Serializable{
	private static final long serialVersionUID = 1L;
	/*** id 主键 ***/
	@Id
	@Column(name="user_id")
	private Integer userId;
	/*** 商户号 ***/
	@Column(length = 20)
	private String merchNo;
	/*** 名称 ***/
	@Column(length = 20)
	private String name;
	/***代理用户**/
	@Column(length = 20)
	private String agentUser;
	/***代理费率***/
	@Column(length = 100)
	private String feeRate;
	/*** RSA 公钥 ***/
	@Column(length = 250)
	private String publicKey;
	
	/***账户余额**/
	@Transient
	private BigDecimal balance;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(String agentUser) {
		this.agentUser = agentUser;
	}

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
