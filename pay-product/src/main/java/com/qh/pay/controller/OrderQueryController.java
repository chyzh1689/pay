package com.qh.pay.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qh.common.utils.PageUtils;
import com.qh.pay.api.Order;
import com.qh.pay.api.constenum.OrderParamKey;
import com.qh.pay.api.constenum.OutChannel;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.redis.service.RedisUtil;

/**
 * @ClassName OrderQueryController
 * @Description 订单查询
 * @author chenyuezhi
 * @Date 2017年11月16日 下午2:28:26
 * @version 1.0.0
 */
@Controller
@RequestMapping("/orderQuery")
public class OrderQueryController {
	
	/***
	 * 
	 * @Description 在途订单
	 * @param model
	 * @return
	 */
	@GetMapping("/order")
	@RequiresPermissions("orderQuery:order")
	String Merchant(Model model){
		model.addAttribute("outChannels", OutChannel.desc());
	    return "pay/orderQuery/order";
	}
	
	@ResponseBody
	@GetMapping("/order/list")
	@RequiresPermissions("orderQuery:order")
	public PageUtils list(@RequestParam Map<String, Object> params){
		String merchNo = (String) params.get(OrderParamKey.merchNo.name());
		String orderNo = (String) params.get(OrderParamKey.orderNo.name());
		List<Order> orders = new ArrayList<>();
		int total = 0;
		if(ParamUtil.isNotEmpty(merchNo) && ParamUtil.isNotEmpty(orderNo)){
			Order order = RedisUtil.getOrder(merchNo, orderNo);
			if(order != null){
				total = 1;
				orders.add(order);
			}
		}else{
			total = orders.size();
		}
		
		PageUtils pageUtils = new PageUtils(orders, total);
		return pageUtils;
	}
}
