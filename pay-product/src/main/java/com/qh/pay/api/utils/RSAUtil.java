package com.qh.pay.api.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;


/**
 * 
 * @ClassName: RSAUtil
 * @Description: rsa加密算法
 * @author chyzh
 * @date 2017年10月24日 下午8:34:20
 *
 */
public class RSAUtil {
	public static class RsaKeyPair {
		private String publicKey = "";
		private String privateKey = "";

		public RsaKeyPair(String publicKey, String privateKey) {
			super();
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}

		public String getPublicKey() {
			return publicKey;
		}

		public String getPrivateKey() {
			return privateKey;
		}
	}

	private static final String ALGORITHM = "RSA";
	private static final String ALGORITHMS_SHA1WithRSA = "SHA1WithRSA";
	private static final String ALGORITHMS_SHA256WithRSA = "SHA256WithRSA";
	private static final String DEFAULT_CHARSET = "UTF-8";


	/**
	 * 生成秘钥对
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static RsaKeyPair generaterKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);
		SecureRandom random = new SecureRandom();
		// SecureRandom random = new SecureRandom(seedStr.getBytes()); //
		// 随机因子一样，生成出来的秘钥会一样
		// 512位已被破解，用1024位,最好用2048位
		keygen.initialize(2048, random);
		// 生成密钥对
		KeyPair keyPair = keygen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
		String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
		return new RsaKeyPair(publicKeyStr, privateKeyStr);
	}

	/**
	 * 获取公钥
	 * 
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKey(String publicKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		return (RSAPublicKey) keyFactory.generatePublic(spec);
	}

	/**
	 * 获取私钥
	 * 
	 * @param privateKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		return (RSAPrivateKey) keyFactory.generatePrivate(spec);
	}

	/**
	 * 私钥签名
	 * 
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public static String sign(String content, String privateKey) throws Exception {
		PrivateKey priKey = getPrivateKey(privateKey);
		java.security.Signature signature = java.security.Signature.getInstance(ALGORITHMS_SHA1WithRSA);
		signature.initSign(priKey);
		signature.update(content.getBytes(DEFAULT_CHARSET));
		byte[] signed = signature.sign();
		return Base64.getEncoder().encodeToString(signed);
	}

	/**
	 * 公钥验签
	 */
	public static boolean verify(String content, String sign, String publicKey) throws Exception {
		PublicKey pubKey = getPublicKey(publicKey);
		java.security.Signature signature = java.security.Signature.getInstance(ALGORITHMS_SHA1WithRSA);
		signature.initVerify(pubKey);
		signature.update(content.getBytes(DEFAULT_CHARSET));
		return signature.verify(Base64.getDecoder().decode(sign));
	}
	/**
	 * 私钥签名
	 * 
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public static String sign2(String content, String privateKey) throws Exception {
		PrivateKey priKey = getPrivateKey(privateKey);
		java.security.Signature signature = java.security.Signature.getInstance(ALGORITHMS_SHA256WithRSA);
		signature.initSign(priKey);
		signature.update(content.getBytes(DEFAULT_CHARSET));
		byte[] signed = signature.sign();
		return Base64.getEncoder().encodeToString(signed);
	}

	/**
	 * 公钥验签
	 */
	public static boolean verify2(String content, String sign, String publicKey) throws Exception {
		PublicKey pubKey = getPublicKey(publicKey);
		java.security.Signature signature = java.security.Signature.getInstance(ALGORITHMS_SHA256WithRSA);
		signature.initVerify(pubKey);
		signature.update(content.getBytes(DEFAULT_CHARSET));
		return signature.verify(Base64.getDecoder().decode(sign));
	}
	/**
	 * 加密
	 * 
	 * @param input
	 * @param pubOrPrikey
	 * @return
	 */
	public static String encrypt(String content, Key pubOrPrikey) throws Exception {
		Cipher cipher = null;
		cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, pubOrPrikey);
		byte[] result = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));
		return Base64.getEncoder().encodeToString(result);
	}

	/**
	 * 解密
	 * 
	 * @param input
	 * @param pubOrPrikey
	 * @return
	 */
	public static String decrypt(String content, Key pubOrPrikey) throws Exception {
		Cipher cipher = null;
		cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, pubOrPrikey);
		byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
		return new String(result);
	}
	
	/**
	 * 公钥加密
	 * 
	 * @param input
	 * @param pubOrPrikey
	 * @return
	 */
	public static String encryptByPublicKey(String content, String publicKey) throws Exception {
		return encrypt(content, getPublicKey(publicKey));
	}
	/**
	 * 私钥加密
	 * 
	 * @param input
	 * @param pubOrPrikey
	 * @return
	 */
	public static String encryptByPrivateKey(String content, String privateKey) throws Exception {
		return encrypt(content, getPrivateKey(privateKey));
	}

	/**
	 * 公钥解密
	 * 
	 * @param input
	 * @param pubOrPrikey
	 * @return
	 */
	public static String decryptByPublicKey(String content, String publicKey) throws Exception {
		return decrypt(content, getPublicKey(publicKey));
	}
	/**
	 * 私钥解密
	 * 
	 * @param input
	 * @param pubOrPrikey
	 * @return
	 */
	public static String decryptByPrivateKey(String content, String privateKey) throws Exception {
		return decrypt(content, getPrivateKey(privateKey));
	}
	
	
	public static void main(String[] args) throws Exception {
		RsaKeyPair rsaKeyPair = generaterKeyPair();
		String publicKey = rsaKeyPair.getPublicKey();
		String privateKey = rsaKeyPair.getPrivateKey();
		System.out.println("公钥：" + publicKey);
		System.out.println("私钥：" + privateKey);
		String content = "陈大侠";
		//私钥加密 公钥解密
		System.out.println(decryptByPublicKey(encryptByPrivateKey(content, privateKey), publicKey));
		//公钥加密 私钥解密
		System.out.println(decryptByPrivateKey(encryptByPublicKey(content, publicKey), privateKey));
		//私钥签名 公钥验签
		System.out.println(verify(content, sign(content, privateKey), publicKey));
		
	}
}