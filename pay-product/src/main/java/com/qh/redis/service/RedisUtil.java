package com.qh.redis.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.qh.common.config.CfgKeyConst;
import com.qh.pay.api.Order;
import com.qh.pay.api.constenum.PayConfigType;
import com.qh.pay.api.utils.AesUtil;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.pay.domain.PayAcctBal;
import com.qh.pay.domain.PayPropertyDO;
import com.qh.redis.RedisConstants;
import com.qh.redis.constenum.ConfigParent;
import com.qh.system.domain.ConfigDO;

/**
 * @ClassName: RedisUtil
 * @Description: redis用到的常用操作
 * @author chyzh
 * @date 2017年10月27日 上午10:26:01
 *
 */
public class RedisUtil {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	private static RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * @Description 支付订单
	 * @param order
	 */
	public static void setOrder(Order order) {
		redisTemplate.opsForHash().put(RedisConstants.cache_order + order.getMerchNo(),  order.getOrderNo(), order);
	}
	
	/**
	 * @Description 获取支付订单
	 * @param merchNo
	 * @param orderNo
	 * @return
	 */
	public static Order getOrder(String merchNo, String orderNo) {
		return 	(Order) redisTemplate.opsForHash().get(RedisConstants.cache_order + merchNo, orderNo);
	}
	
	
	/**
	 * @Description 删除支付订单
	 * @param msgKey
	 */
	public static void removeOrder(String merchNo, String orderNo) {
		redisTemplate.opsForHash().delete(RedisConstants.cache_order + merchNo, orderNo);
		
	}

	
	/**
	 * @Description 代付订单
	 * @param order
	 */
	public static void setOrderAcp(Order order) {
		redisTemplate.opsForHash().put(RedisConstants.cache_order_acp +  order.getMerchNo(), order.getOrderNo(), order);
	}
	
	/**
	 * @Description 获取支付订单
	 * @param merchNo
	 * @param orderNo
	 * @return
	 */
	public static Order getOrderAcp(String merchNo, String orderNo) {
		return 	(Order) redisTemplate.opsForHash().get(RedisConstants.cache_order_acp + merchNo, orderNo);
	}
	
	/**
	 * @Description 删除代付订单
	 * @param msgKey
	 */
	public static void removeOrderAcp(String merchNo, String orderNo) {
		redisTemplate.opsForHash().delete(RedisConstants.cache_order_acp + merchNo, orderNo);
		
	}

	/**
	 * @Description 获取商户余额
	 * @param merchNo
	 */
	public static PayAcctBal getMerchBal(String merchNo) {
		return (PayAcctBal) redisTemplate.opsForHash().get(RedisConstants.cache_bal_merch, merchNo);
	}
	
	public static void setMerchBal(PayAcctBal payAcctBal){
		 redisTemplate.opsForHash().put(RedisConstants.cache_bal_merch, payAcctBal.getUsername(), payAcctBal);
	}
	
	/**
	 * @Description 获取代理余额
	 * @param merchNo
	 */
	public static PayAcctBal getAgentBal(String username) {
		return (PayAcctBal) redisTemplate.opsForHash().get(RedisConstants.cache_bal_agent, username);
	}
	
	public static void setAgentBal(PayAcctBal payAcctBal){
		 redisTemplate.opsForHash().put(RedisConstants.cache_bal_agent, payAcctBal.getUsername(), payAcctBal);
	}
	/**
	 * 
	 * @Description 获取支付参数配置的值
	 * @param key
	 * @return
	 */
	public static String getPayValue(String key){
		String value = getPayCommonValue(key);
		if(ParamUtil.isEmpty(value)){
			value = getPayFilePathValue(key);
		}
		if(ParamUtil.isEmpty(value)){
			value = getPayIpValue(key);
		}
		return value;
	}
	/**
	 * 
	 * @Description 获取改支付公司下的商户号
	 * @param payCompany
	 * @return
	 */
	public static Set<Object> getMechNoByCompany(String payCompany){
    	return redisTemplate.boundSetOps(RedisConstants.cache_payConfig + PayConfigType.merchantNo.id() + payCompany).members();
	}
	
	/***
	 * 
	 * @Description 获取资金账户信息
	 * @param template
	 */
	public static PayAcctBal getPayFoundBal(){
		return (PayAcctBal) redisTemplate.opsForValue().get(RedisConstants.cache_bal_foundAcct);
	}
	
	public static void setPayFoundBal(PayAcctBal payAcctBal){
		redisTemplate.opsForValue().set(RedisConstants.cache_bal_foundAcct,payAcctBal);
	}
	
	

	public static void syncConfig(ConfigDO config, boolean delateFlag){
		if(config == null){
			return;
		}
		if(ParamUtil.isNotEmpty(config.getParentItem())){
			if(delateFlag){
				redisTemplate.boundHashOps(RedisConstants.cache_config_parent + config.getParentItem()).delete(config.getConfigItem());
			}else{
				redisTemplate.boundHashOps(RedisConstants.cache_config_parent + config.getParentItem()).put(config.getConfigItem(), config.getConfigValue());
			}
		}
	}
	
	public static String getConfigValue(String configItem,String parentItem){
		return (String) redisTemplate.boundHashOps(RedisConstants.cache_config_parent + parentItem).get(configItem);
	}
	
	public static String getSysConfigValue(String configItem){
		return (String) redisTemplate.boundHashOps(RedisConstants.cache_config_parent + ConfigParent.sysConfig.name()).get(configItem);
	}
	
	public static void delConfig(String configItem, String parentItem) {
		if(ParamUtil.isNotEmpty(parentItem)){
			redisTemplate.boundHashOps(RedisConstants.cache_config_parent + parentItem).delete(configItem);
		}
	}
	
	
	/**
	 * @Description 同步支付配置信息
	 * @param payProperty
	 */
	public static void syncPayConfig(PayPropertyDO payProperty) {
		if(payProperty != null){
			Integer configType = payProperty.getConfigType();
			if(configType == null){
				return;
			}
			String key = payProperty.getConfigKey();
			if(ParamUtil.isNotEmpty(payProperty.getMerchantno())){
				key = payProperty.getMerchantno() + key;
			}
			String value = payProperty.getValue();
			if(PayConfigType.pass.id() == payProperty.getConfigType()){
                value = AesUtil.decrypt(value);
                redisTemplate.boundHashOps(RedisConstants.cache_payConfig).put(key, value);
            }else  if(PayConfigType.ip.id() == payProperty.getConfigType()){
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig).delete(key);
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig + PayConfigType.ip.id()).put(key, value);
            }else if(PayConfigType.filePath.id()== payProperty.getConfigType()){
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig).delete(key);
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig + PayConfigType.filePath.id()).put(key, value);
            }else if(PayConfigType.fileValue.id() == payProperty.getConfigType()){
            	String payFilePath = getSysConfigValue(CfgKeyConst.payFilePath) + value;
            	try {
					redisTemplate.boundHashOps(RedisConstants.cache_payConfig).put(key, 
							ParamUtil.readTxtFileFilter(payFilePath));
				} catch (Exception e) {
					logger.error("支付配置文件加载失败！{}", payFilePath);
					throw new RuntimeException("支付配置文件加载失败" + payFilePath);
				}
            }else if(PayConfigType.merchantNo.id() == payProperty.getConfigType()){
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig).put(key, value);
            	redisTemplate.boundSetOps(RedisConstants.cache_payConfig + PayConfigType.merchantNo.id() + payProperty.getPayCompany()).add(value);
            }else {
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig).put(key, value);
            }
		}
		
	}
	
	public static String getPayFilePathValue(String key){
		String value = getPayValue(key, PayConfigType.filePath);
		if(ParamUtil.isNotEmpty(value)){
			value = getSysConfigValue(CfgKeyConst.payFilePath) + value;
		}
		return value;
	}
	
	public static String getPayIpValue(String key){
		String value = getPayValue(key, PayConfigType.ip);
		if(ParamUtil.isNotEmpty(value)){
			value = getSysConfigValue(CfgKeyConst.ip) + value;
		}
		return value;
	}
	
	public static String getPayValue(String key,PayConfigType configType){
		return (String) redisTemplate.boundHashOps(RedisConstants.cache_payConfig + configType.id()).get(key);
	}
	
	public static String getPayCommonValue(String key){
		return (String) redisTemplate.boundHashOps(RedisConstants.cache_payConfig).get(key);
	}
	
	/**
	 * @Description 删除支付配置
	 * @param payPropertyDO
	 */
	public static void delPayConfig(PayPropertyDO payPropertyDO) {
		if(payPropertyDO != null){
			delPayConfig(payPropertyDO.getConfigKey(),payPropertyDO);
		}
		
	}
	/**
	 * @Description 删除支付配置
	 * @param configKey
	 * @param payProperty 
	 */
	public static void delPayConfig(String configKey, PayPropertyDO payProperty) {
		if(payProperty == null){
			return;
		}
		if(ParamUtil.isNotEmpty(payProperty.getMerchantno())){
			configKey = payProperty.getMerchantno() + configKey;
		}
		Integer configType = payProperty.getConfigType();
		if(configType != null){
			if(PayConfigType.ip.id() == configType){
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig + PayConfigType.ip.id()).delete(configKey);
            }else if(PayConfigType.filePath.id()== configType){
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig + PayConfigType.filePath.id()).delete(configKey);
            }else if(PayConfigType.merchantNo.id() == configType){
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig).delete(configKey);
            	redisTemplate.boundSetOps(RedisConstants.cache_payConfig + PayConfigType.merchantNo.id() + payProperty.getPayCompany()).remove(payProperty.getValue());
            }else{
            	redisTemplate.boundHashOps(RedisConstants.cache_payConfig).delete(configKey);
            }
		}else{
			redisTemplate.boundHashOps(RedisConstants.cache_payConfig).delete(configKey);
		}
	}
	
	public static Map<Object, Object> getCacheMap(String key){
		return redisTemplate.boundHashOps(key).entries();
	}
	
	public static ConfigDO getCacheConfig(String key){
		return (ConfigDO) redisTemplate.boundValueOps(RedisConstants.cache_config).
				getOperations().boundValueOps(key).get();
	}
	
	public static Map<String, Object> getCacheMapDesc(String key){
		Map<Object, Object> cacheMap =  getCacheMap(key);
		Map<String, Object> descMap = new HashMap<String,Object>();
		if(!cacheMap.isEmpty()){
			ConfigDO configDO = null;
			for (Entry<Object, Object> entry : cacheMap.entrySet()) {
				configDO = getCacheConfig((String)entry.getKey());
				if(configDO != null){
					descMap.put((String)entry.getValue(), configDO.getConfigName());
				}
			}
			return descMap;
		}else{
			return descMap;
		}
	}

	public static void setRedisTemplate(RedisTemplate<String, Object> template) {
		redisTemplate = template;
	}
	
	public static RedisTemplate<String, Object> getRedisTemplate(){
		return redisTemplate;
	}

}
