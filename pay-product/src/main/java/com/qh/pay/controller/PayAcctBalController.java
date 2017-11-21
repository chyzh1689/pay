package com.qh.pay.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qh.pay.api.constenum.UserType;
import com.qh.pay.domain.PayAcctBal;
import com.qh.pay.service.PayAcctBalService;
import com.qh.common.utils.PageUtils;
import com.qh.common.utils.Query;

/**
 * 账号余额表
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-06 11:41:35
 */
 
@Controller
@RequestMapping("/pay/payAcctBal")
public class PayAcctBalController {
	@Autowired
	private PayAcctBalService payAcctBalService;
	
	@GetMapping()
	@RequiresPermissions("pay:payAcctBal:payAcctBal")
	String PayAcctBal(Model model){
		model.addAttribute("userTypes", UserType.desc());
	    return "pay/payAcctBal/payAcctBal";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("pay:payAcctBal:payAcctBal")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PayAcctBal> payAcctBalList = payAcctBalService.list(query);
		int total = payAcctBalService.count(query);
		PageUtils pageUtils = new PageUtils(payAcctBalList, total);
		return pageUtils;
	}
	
}
