package com.qh.pay.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qh.pay.api.constenum.OutChannel;
import com.qh.pay.api.utils.RSAUtil;
import com.qh.pay.api.utils.RequestUtils;

/**
 * @ClassName PayServiceTest
 * @Description 支付测试
 * @author chenyuezhi
 * @Date 2017年10月31日 上午11:42:01
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.qh.pay")
public class PayServiceTest {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PayServiceTest.class);
	/****
	 * 商户号
	 */
	public final static String merchNo = "QH0000";
	/**
	 * 公钥 --启晗
	 */
	public final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMlNqJh3JG6shlMJ0OJ42QnuG9OVUiBlcpbUXbaaprUjF1XTqDaUJZLvk5fkRDAgZAC/CbyYOOoZBpp8y3CnnCSPtJ8oKoLuQOcN1hW4snE0VP+J2wKMQQyjmzFK4MiRRDE6oxD2nWFe517zl8IOJYZWK3egTIXezoidLG0bucZwIDAQAB";
	/**
	 * 公钥 --商户
	 */
	public final static String mcPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFr5fSL3N0qa+tpIRFn/ApDfeuOMrvhz3Cb3T94by7KigO57ppkMadAOG2wLV5S6QA5WeN5oZWHzNUnYZbn6cFE38cV8LX0ABMl0A0x5O00NCMTCkxxUZ/5IlrK6SYEjk75vSiimtlAI9ZW/F8RKqzVoOr5pHZJ4tRSXaR5VHO0wIDAQAB";
	/**
	 * 私钥---商户
	 */
	public final static String mcPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIWvl9Ivc3Spr62khEWf8CkN9644yu+HPcJ"
			+ "vdP3hvLsqKA7nummQxp0A4bbAtXlLpADlZ43mhlYfM1SdhlufpwUTfxxXwtfQAEyXQDTHk7TQ0IxMKTHFRn/kiWsrpJgSOTvm9KKKa2"
			+ "UAj1lb8XxEqrNWg6vmkdkni1FJdpHlUc7TAgMBAAECgYBNlfoDtxxHoc9edHN7wPXtrbiIOVe1qgSy2mLIkYEqEq5K8Dvk1mweZIuat"
			+ "77alYaqKnluBlMCmnr86as3c7HHTQlh8tlOOSnmwLzacVF453FvKAjvH9ti1nSf6dk9yCoDcsgulOYnqqRbAvVg+evBmmWuIVqZxvwe"
			+ "CxNERo98CQJBANFPtCSJdzMDk0uiE0r9nwiESYyX1n0NCozHKc6kSuGilx30xrrcedMbZyKDTCYgogp+d+QEzYddqq0Gj67jtXcCQQ"
			+ "CjgXQjgaFfUEBsFQ2menYGQgawCGnxYCJ7oUlBUScJrFpFhosHcBaoq69acQyGkC6kOu/jjuODjAAzjUVn4biFAkEAlu9tzOcgALZ0U"
			+ "hb26J3JP5/9VZfsgNKVp/y6phuNL/ZKGLz5TahNZTEehyG9GMVxdDXMiK3588JUoF7Z39iucwJBAJYsUyA9cprZWaIroBL0zSwoPn41"
			+ "7CBPPLyyQVclkyZWT78luNIHCDi5H2CBDpEVIlGi9CvcVGjBEHpI2aN09QUCQCg0j5IEsCeinYje4Pjs6v8y6GdiW6qUl8p2pol1LBt"
			+ "R/ycMcYkJWSUN1Ffgz84cRCkxuLS6oxyyyLporbj4kig=";
	/***
	 * 支付域名
	 */
	public final static String url = "http://localhost:8888/pay/order";
	
	@Test
	public void publick_length(){
		System.out.println(publicKey.length());
	}
	/**
	 * 
	 *订单支付测试
	 * @throws Exception 
	 */
	@Test
	public void order_test() throws Exception{
		JSONObject jsObj = new JSONObject();
		String reqTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		//商户号
		jsObj.put("merchNo", merchNo);
		//订单号
		jsObj.put("orderNo", reqTime + new Random().nextInt(10000));	
		//支付渠道
		jsObj.put("outChannel", OutChannel.wy.name());
		if(OutChannel.wy.name().equals(jsObj.get("outChannel"))){
			jsObj.put("bankName", "建设银行");
		}
		//订单标题 
		jsObj.put("title", "商城网银下单");
		//产品名称
		jsObj.put("product", "产品名称");
		//支付金额 单位 元 
		jsObj.put("amount", String.valueOf(new Random().nextInt(10000)));
		//币种
		jsObj.put("currency", "CNY");
		//前端返回地址
		jsObj.put("returnUrl", "http://www.baidu.com");
		//后台通知地址
		jsObj.put("notifyUrl", "http://www.baidu.com");
		//请求时间
		jsObj.put("reqTime", reqTime);
		
		byte[] context = RSAUtil.encryptByPublicKey(JSON.toJSONBytes(jsObj), publicKey);
		String sign = RSAUtil.sign(context, mcPrivateKey);
		logger.info("签名结果：{}" ,sign);
		JSONObject jo = new JSONObject();
		jo.put("sign", sign);
		jo.put("context", context);
		logger.info("请求参数：{}", jo.toJSONString());
		String result = RequestUtils.doPostJson(url, jo.toJSONString());
		logger.info("请求结果！{}",result);
		jo = JSONObject.parseObject(result);
		if("0".equals(jo.getString("code"))){
			sign = jo.getString("sign");
			context = jo.getBytes("context");
			if(RSAUtil.verify(context, publicKey, sign)){
				String source = new String(RSAUtil.decryptByPrivateKey(context, mcPrivateKey));
				logger.info("解密结果：" + source);
				jo = JSONObject.parseObject(source);
				logger.info("网银支付链接地址：{}", jo.getString("code_url"));
				
			}else{
				logger.info("验签失败！{}");
			}
		}
	}
	
	/**
	 * 
	 * @Description 支付订单查询
	 * @throws Exception
	 */
	@Test
	public void order_query() throws Exception{
		String orderNo = "201711151647288410";
		JSONObject jsObj = new JSONObject();
		//商户号
		jsObj.put("merchNo", merchNo);
		jsObj.put("orderNo", orderNo);
		byte[] context = RSAUtil.encryptByPublicKey(JSON.toJSONBytes(jsObj), publicKey);
		String sign = RSAUtil.sign(context, mcPrivateKey);
		logger.info("签名结果：{}" ,sign);
		JSONObject jo = new JSONObject();
		jo.put("sign", sign);
		jo.put("context", context);
		logger.info("请求参数：{}", jo.toJSONString());
		String result = RequestUtils.doPostJson(url + "/query", jo.toJSONString());
		logger.info("请求结果！{}",result);
		jo = JSONObject.parseObject(result);
		if("0".equals(jo.getString("code"))){
			sign = jo.getString("sign");
			context = jo.getBytes("context");
			if(RSAUtil.verify(context, publicKey, sign)){
				String source = new String(RSAUtil.decryptByPrivateKey(context, mcPrivateKey));
				logger.info("解密结果：" + source);
				jo = JSONObject.parseObject(source);
				logger.info("订单支付状态", jo.getString("orderState"));
				
			}else{
				logger.info("验签失败！{}");
			}
		}
	}
	
	
	/**
	 * 
	 * @Description 代付订单
	 * @throws Exception
	 */
	@Test
	public void order_acp() throws Exception{
		JSONObject jsObj = new JSONObject();
		String reqTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		//商户号
		jsObj.put("merchNo", merchNo);
		//订单号
		jsObj.put("orderNo", reqTime + new Random().nextInt(10000));	
		//支付渠道  ---acp
		jsObj.put("outChannel", OutChannel.acp.name());
		jsObj.put("bankName", "建设银行");
		//订单标题 
		jsObj.put("title", "商城网银代付");
		//产品名称
		jsObj.put("product", "产品名称");
		//代付付金额 单位 元 
		jsObj.put("amount", String.valueOf(new Random().nextInt(10000)));
		//币种
		jsObj.put("currency", "CNY");
		//后台通知地址
		jsObj.put("notifyUrl", "http://www.baidu.com");
		//请求时间
		jsObj.put("reqTime", reqTime);
		
		byte[] context = RSAUtil.encryptByPublicKey(JSON.toJSONBytes(jsObj), publicKey);
		String sign = RSAUtil.sign(context, mcPrivateKey);
		logger.info("签名结果：{}" ,sign);
		JSONObject jo = new JSONObject();
		jo.put("sign", sign);
		jo.put("context", context);
		logger.info("请求参数：{}", jo.toJSONString());
		String result = RequestUtils.doPostJson(url + "/acp", jo.toJSONString());
		logger.info("请求结果！{}",result);
		jo = JSONObject.parseObject(result);
		if("0".equals(jo.getString("code"))){
			sign = jo.getString("sign");
			context = jo.getBytes("context");
			if(RSAUtil.verify(context, publicKey, sign)){
				String source = new String(RSAUtil.decryptByPrivateKey(context, mcPrivateKey));
				logger.info("解密结果：" + source);
				jo = JSONObject.parseObject(source);
				logger.info("代付消息：{}", jo.getString("msg"));
				
			}else{
				logger.info("验签失败！{}");
			}
		}
	}
	
}
