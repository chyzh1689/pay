package com.qh.pay.api.constenum;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: OutChannel
 * @Description: 支付通道枚举类 微信WAP支付：wap 微信公众号支付：gzh 微信扫码：wx QQ钱包扫码：qq
 *               支付宝扫码：ali 快捷支付：q 网银支付：wy 代付：acp
 * @author chyzh
 * @date 2017年10月24日 下午8:02:11
 *
 */
public enum OutChannel {
	wap, gzh, wx, qq, ali,q,wy, acp,sp;
	/****支付渠道****/
	private static final Map<String,String> descMap = new HashMap<>(16);
	static{
		descMap.put(acp.name(), "代付");
		descMap.put(ali.name(), "支付宝扫码");
		descMap.put(wy.name(), "网银");
		descMap.put(gzh.name(), "公众号");
		descMap.put(q.name(), "快捷");
		descMap.put(qq.name(), "QQ钱包扫码");
		descMap.put(sp.name(), "商铺收款");
		descMap.put(wap.name(), "微信WAP");
		descMap.put(wx.name(), "微信扫码");
	}
	public static Map<String, String> desc() {
		return descMap;
	}

}
