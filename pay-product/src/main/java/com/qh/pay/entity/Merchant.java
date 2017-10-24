package com.qh.pay.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Merchant {

	/*** id 主键 ***/
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	/*** 商户名 ***/
	@Column(length = 20)
	private String username;
	/*** 名称 ***/
	@Column(length = 20)
	private String name;
	/*** 密钥 ***/
	@Column(length = 150)
	private String md5Key;
	/*** RSA 公钥 ***/
	@Column(length = 150)
	private String publicKey;
	/*** 余额 ***/
	@Column
	private BigDecimal balance;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
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
