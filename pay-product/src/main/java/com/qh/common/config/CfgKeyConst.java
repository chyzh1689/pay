package com.qh.common.config;

/**
 * @ClassName ConfigKeyConstant
 * @Description 系统级别的配置常量key
 * @author chenyuezhi
 * @Date 2017年10月30日 下午4:33:16
 * @version 1.0.0
 */
public class CfgKeyConst {
	/***系统配置参数 ip域名******/
    public static final String ip = "ip";
    /***系统配置文件路径***/
    public static final String payFilePath = "payFilePath";
    /***启晗公钥***/
    public static final String qhPublicKey = "qhPublicKey";
    /***启晗私钥***/
    public static final String qhPrivateKey = "qhPrivateKey";
    /***启晗商户默认密码***/
    public static final String pass_default_merch = "pass_default_merch";
    /***启晗商户默认状态***/
    public static final String state_default_merch = "state_default_merch";
    /***启晗支付域名****/
    public static final String pay_domain = "pay_domain";
    /***启晗前台回调设置***/
    public static final String pay_return_url = "pay_return_url";
    /***启晗后台通知设置***/
    public static final String pay_notify_url = "pay_notify_url";
    /***启晗代付前台回调设置***/
    public static final String pay_acp_return_url = "pay_acp_return_url";
    /***启晗代付后台通知设置***/
    public static final String pay_acp_notify_url = "pay_acp_notify_url";
}
