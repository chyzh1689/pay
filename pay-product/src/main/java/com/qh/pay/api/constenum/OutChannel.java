package com.qh.pay.api.constenum;

/**
 * 
 * @ClassName: OutChannel
 * @Description: 支付通道枚举类 微信WAP支付：wappay 微信公众号支付：gzhpay 微信扫码：wxpay QQ钱包扫码：qqpay
 *               支付宝扫码：alipay 快捷支付：qpay 网银支付：cardpay 代付：acp
 * @author chyzh
 * @date 2017年10月24日 下午8:02:11
 *
 */
public enum OutChannel {
	oc_wappay("wappay"), oc_gzhpay("gzhpay"), oc_wxpay("wxpay"), oc_qqpay("qqpay"), oc_alipay("alipay"), oc_qpay(
			"qpay"), oc_cardpay("cardpay"), oc_acp("acp");

	private String name;

	private OutChannel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
