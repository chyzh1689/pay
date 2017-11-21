package com.qh.pay.api.utils;

/**
 * @ClassName QhPayUtil
 * @Description 支付工具类
 * @author chenyuezhi
 * @Date 2017年10月31日 上午11:44:01
 * @version 1.0.0
 */
public class QhPayUtil {
	/**
	 * qh密钥
	 */
	private static final String qhpayKey = "NsXpt407QKN2zq/5x4gK/Q==";

	/**
	 * 商户号前缀
	 */
	public static final String merchNoPrefix = "QH"; 
	
	/***
	 * qh 公钥
	 */
	private static String qhPublicKey = "";
	
	/**
	 * qh私钥
	 */
	private static String qhPrivateKey = "";
	/**
	 * 
	 * @Description 加密
	 * @param content
	 * @return
	 */
	public static String encrypt(String content){
		return AesUtil.encrypt(content, qhpayKey);
	}
	
	/**
	 * 
	 * @Description 解密
	 * @param result
	 * @return
	 */
	public static String decrypt(String result){
		return AesUtil.decrypt(result, qhpayKey);
	};
	
	/**
	 * 
	 * @Description 获取启晗公钥
	 * @return
	 */
	public static String getQhPublicKey(){
		return QhPayUtil.qhPublicKey;
	}

	public static void setQhPublicKey(String publicKey){
		QhPayUtil.qhPublicKey = publicKey;
	}
	/**
	 * 
	 * @Description 获取启晗私钥
	 * @return
	 */
	public static String getQhPrivateKey(){
		return QhPayUtil.qhPrivateKey;
	}

	public static void setQhPrivateKey(String privateKey){
		QhPayUtil.qhPrivateKey = privateKey;
	}

}
