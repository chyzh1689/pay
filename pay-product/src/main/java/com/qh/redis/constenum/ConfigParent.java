package com.qh.redis.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: 配置父类
 * @Description: payComapny:支付公司 sysConfig:系统配置
 * @author chyzh
 * @date 2017年10月24日 下午8:02:11
 *
 */
public enum ConfigParent {
	payCompany,sysConfig;
	/****系统参数配置****/
	public static final Map<String,String> descMap = new HashMap<>(8);
	static{
		descMap.put(ConfigParent.payCompany.name(), "支付公司");
		descMap.put(ConfigParent.sysConfig.name(), "系统初始");
	}
}
