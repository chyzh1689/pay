package com.qh.redis;

/**
 * 
 * @ClassName: RedisConstants
 * @Description: 常用的常量
 * @author chyzh
 * @date 2017年10月25日 下午9:11:21
 */
public class RedisConstants {
	
    /** 默认超时时间（毫秒） */  
    public static final long DEFAULT_TIME_OUT = 1000; 
    
    /***连接符**/
	public static final String link_symbol = "_";
    
    /***用户缓存**/
    public static final String cache_user = "user_";
    
    /***配置缓存**/
    public static final String cache_config = "cfg_";
    
    /***父类配置缓存**/
    public static final String cache_config_parent = "cfg_p_";
    
    /***配置缓存**/
    public static final String cache_payConfig = "payCfg_";
    
    /***支付配置缓存****/
    public static final String cache_payConfigCompany = "payCfgCmp_";
    
    /***启晗商户****/
    public static final String cache_merchant = "merch_";
    
    /***余额账户缓存 ---平台资金账户***/
    public static final String cache_bal_foundAcct = "bal_foundAcct_";
    
    /***余额账户缓存 ---启晗商户***/
    public static final String cache_bal_merch = "bal_merch_";
    
    /***同步余额账户缓存 ---启晗商户***/
    public static final String lock_bal_merch = "lock_merch_";
    
    /***余额账户缓存 ---启晗代理***/
    public static final String cache_bal_agent = "bal_agent_";
    
    /***同步余额账户缓存 ---启晗代理***/
    public static final String lock_bal_agent = "lock_agent_";
    
    /***同步余额账户缓存 ---平台资金账户***/
    public static final String lock_bal_foundAcct = "lock_foundAcct_";
    
    /***订单列表*****/
    public static final String cache_order = "ord_";
    
    /***代付订单列表****/
    public static final String cache_order_acp = "ord_acp_";
    
    /***订单列表 同步*****/
    public static final String lock_order = "lock_ord_";
    
    /***代付订单列表 同步****/
    public static final String lock_order_acp = "lock_ord_acp_";
    
    /***订单渠道***/
    public static final String channel_order = "chl_ord";
    
    /***订单渠道***/
    public static final String channel_order_notify = "chl_ord_not";
    
    /***订单数据保存**/
    public static final String channel_order_data = "chl_ord_data";
    
    /***代付订单下单***/
    public static final String channel_order_acp = "chl_ord_acp";
    
    /***订单渠道***/
    public static final String channel_order_acp_notify = "chl_ord_acp_not";
    
    /***代付订单未通过***/
    public static final String channel_order_acp_nopass = "chl_ord_acp_nopass";
    
    /***代付订单数据保存**/
    public static final String channel_order_acp_data = "chl_ord_acp_data";
}
