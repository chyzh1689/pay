package com.qh.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qh.common.config.Constant;
import com.qh.common.utils.R;
import com.qh.pay.api.Order;
import com.qh.pay.api.constenum.PayCompany;
import com.qh.pay.service.PayService;
import com.qh.redis.service.RedisUtil;

/**
 * @ClassName PayNotifyController
 * @Description 回调
 * @author chenyuezhi
 * @Date 2017年11月9日 下午2:26:14
 * @version 1.0.0
 */
@RestController
@RequestMapping("/pay")
public class PayNotifyController {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PayNotifyController.class);

	
	@Autowired
	private PayService payService;
	
	/**
	 * 
	 * @Description 支付前台回调
	 * @param company
	 * @param merchNo
	 * @param orderNo
	 * @param response
	 */
	@GetMapping("/return/{company}/{merchNo}/{orderNo}")
	public void returnUrl(@PathVariable("company") String company, @PathVariable("merchNo") String merchNo, @PathVariable("orderNo") String orderNo, 
			HttpServletResponse response){
		logger.info("前台回调：{},{},{}",company,merchNo,orderNo);
		Order order = RedisUtil.getOrder(merchNo,orderNo);
		if(order != null){
			try {
				response.sendRedirect(order.getReturnUrl());
			} catch (IOException e) {
				logger.error("{}返回异常！", order.getReturnUrl());
			}
		}
		handlerReturnError(company, merchNo,orderNo,response);
	}
	
	/**
	 * @param orderNo 
	 * @param merchNo 
	 * @param response 
	 * @Description 处理错误结果
	 */
	private void handlerReturnError(String company, String merchNo, String orderNo, HttpServletResponse response) {
		try {
			PrintWriter pw = response.getWriter();
			pw.write("订单不存在！" + company + ","+ merchNo + "," + orderNo);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			logger.error("handlerReturnError异常！{},{},{}",company,merchNo,orderNo);
		}
		
	}

	/**
	 * 
	 * @Description 支付后台通知
	 * @param company
	 * @param merchNo
	 * @param orderNo
	 * @param request
	 * @param requestBody
	 * @return
	 */
	@PostMapping("/notify/{company}/{merchNo}/{orderNo}")
	public String notifyUrl(@PathVariable("company") String company, @PathVariable("merchNo") String merchNo, @PathVariable("orderNo") String orderNo,
			HttpServletRequest request,@RequestBody String requestBody){
		logger.info("后台通知：{},{},{}",company,merchNo,orderNo);
		if(RedisUtil.getOrder(merchNo, orderNo) != null){
			R  r = payService.notify(merchNo,orderNo,request, requestBody);
			return notifyStr(String.valueOf(r.get(Constant.result_code)), company);
		}
		return notifyStr(String.valueOf(Constant.result_code_succ), company);
	}

	
	public static final Map<String,String> notifyStrMap = new HashMap<>();
	static{
		notifyStrMap.put(Constant.result_code_succ + PayCompany.bopay.name() , "result=SUCCESS");
		notifyStrMap.put(Constant.result_code_error + PayCompany.bopay.name(), "result=failed");
	}
	
	/**
	 * @Description 返回提示
	 * @param code
	 * @param company
	 * @return
	 */
	private String notifyStr(String code,String company) {
		return notifyStrMap.get(code + company);
	}
	
	/**
	 * 
	 * @Description 代付后台通知
	 * @param company
	 * @param merchNo
	 * @param orderNo
	 * @param request
	 * @param requestBody
	 * @return
	 */
	@PostMapping("/notify/acp/{company}/{merchNo}/{orderNo}")
	public String notifyAcpUrl(@PathVariable("company") String company, @PathVariable("merchNo") String merchNo, @PathVariable("orderNo") String orderNo,
			HttpServletRequest request,@RequestBody String requestBody){
		logger.info("后台通知：{},{},{}",company,merchNo,orderNo);
		if(RedisUtil.getOrder(merchNo, orderNo) != null){
			R  r = payService.notifyAcp(merchNo,orderNo,request, requestBody);
			return notifyStr(String.valueOf(r.get(Constant.result_code)), company);
		}
		return notifyStr(String.valueOf(Constant.result_code_succ), company);
	}
}
