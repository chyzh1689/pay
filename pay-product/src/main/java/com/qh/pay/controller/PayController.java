package com.qh.pay.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qh.common.config.Constant;
import com.qh.common.controller.BaseController;
import com.qh.common.utils.R;
import com.qh.pay.api.constenum.OrderParamKey;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.pay.api.utils.QhPayUtil;
import com.qh.pay.api.utils.RSAUtil;
import com.qh.pay.api.utils.RequestUtils;
import com.qh.pay.domain.Merchant;
import com.qh.pay.service.MerchantService;
import com.qh.pay.service.PayService;
/**
 * 
 * @ClassName PayController
 * @Description pay
 * @author chenyuezhi
 * @Date 2017年10月24日 上午11:30:22
 * @version 1.0.0
 */
@RestController
@RequestMapping("/pay")
public class PayController  extends BaseController{

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PayController.class);
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private PayService payService;
    
    @GetMapping("/merchant/{merchNo}")
    public Merchant findByMerchNo(@PathVariable String merchNo){
        return merchantService.get(merchNo);
    }
    
    /**
     * 
     * @Description 支付下单
     * @param request
     * @return
     */
    @PostMapping("/order")
    public Object order(HttpServletRequest request){
    	R r =  commDataCheck(request);
    	if(R.ifSucc(r)){
    		return payService.order((Merchant)r.get(Constant.param_merch), (JSONObject)r.get(Constant.param_jsonData));
    	}else{
    		return r;
    	}
    }

	
    /**
     * 
     * @Description 代付下单
     * @param request
     * @return
     */
    @PostMapping("/order/acp")
    public Object orderAcp(HttpServletRequest request){
    	R r =  commDataCheck(request);
    	if(R.ifSucc(r)){
    		return payService.orderAcp((Merchant)r.get(Constant.param_merch), (JSONObject)r.get(Constant.param_jsonData));
    	}else{
    		return r;
    	}
    }
    /**
     * 
     * @Description 支付查询
     * @param request
     * @return
     */
    @PostMapping("/order/query")
    public Object query(HttpServletRequest request){
    	R r =  commDataCheck(request);
    	if(R.ifSucc(r)){
    		return payService.query((Merchant)r.get(Constant.param_merch), (JSONObject)r.get(Constant.param_jsonData));
    	}else{
    		return r;
    	}
    }
    /**
     * 
     * @Description 代付查询
     * @param request
     * @return
     */
    @PostMapping("/order/acp/query")
    public Object acpQuery(HttpServletRequest request){
    	R r =  commDataCheck(request);
    	if(R.ifSucc(r)){
    		return payService.acpQuery((Merchant)r.get(Constant.param_merch), (JSONObject)r.get(Constant.param_jsonData));
    	}else{
    		return r;
    	}
    }
    /**
	 * @Description 通用检查方法
	 * @param request
	 * @return
	 */
	private R commDataCheck(HttpServletRequest request) {
		JSONObject jsonObject =  RequestUtils.getJsonResultStream(request);
    	if(jsonObject == null){
    		return R.error("请检查请求参数！");
    	}
    	String sign = jsonObject.getString("sign");
    	logger.info("请求签名：{}",sign);
    	if(ParamUtil.isEmpty(sign)){
    		return R.error("请检查签名参数！");
    	}
    	byte[] context = jsonObject.getBytes("context");
    	if(ParamUtil.isEmpty(context)){
    		return R.error("请检查加密内容！");
    	}
    	logger.info("请求加密结果：{}", context);
    	try {
    		//解密
    		String source = new String(RSAUtil.decryptByPrivateKey(context, QhPayUtil.getQhPrivateKey()));
    		logger.info("解密结果！" + source);
    		JSONObject jo = JSON.parseObject(source);
    		String merchNo = jo.getString(OrderParamKey.merchNo.name());
    		if(ParamUtil.isEmpty(merchNo)){
    			logger.error("解密失败或者商户号为空！" + source);
    			return R.error("解密失败或者商户号为空！" + source);
    		}
    		Merchant merchant = merchantService.get(merchNo);
			if(merchant == null){
				logger.error("商户不存在！" + merchNo);
				return R.error("商户不存在！" + merchNo);
			}
			if(RSAUtil.verify(context, merchant.getPublicKey(), sign)){
				logger.info("验签成功！", merchant.getPublicKey());
				return R.ok().put(Constant.param_merch, merchant).put(Constant.param_jsonData, jo);
			}else{
				logger.error("验签失败！" + merchNo);
				return R.error("验签失败！" + merchNo);
			}
		} catch (Exception e) {
			return R.error("支付异常！" + e.getMessage());
		}
	}
}