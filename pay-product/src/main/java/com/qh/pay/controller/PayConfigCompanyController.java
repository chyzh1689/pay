package com.qh.pay.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qh.pay.api.constenum.OutChannel;
import com.qh.pay.api.constenum.PayCompany;
import com.qh.pay.api.utils.DateUtil;
import com.qh.pay.domain.PayConfigCompanyDO;
import com.qh.pay.service.PayConfigCompanyService;
import com.qh.common.utils.PageUtils;
import com.qh.common.utils.Query;
import com.qh.common.utils.R;

/**
 * 支付公司配置
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-06 16:00:33
 */
 
@Controller
@RequestMapping("/pay/payConfigCompany")
public class PayConfigCompanyController {
	@Autowired
	private PayConfigCompanyService payConfigCompanyService;
	
	@GetMapping()
	@RequiresPermissions("pay:payConfigCompany:payConfigCompany")
	String PayConfigCompany(Model model){
		model.addAttribute("payCompanys", PayCompany.desc());
		model.addAttribute("outChannels", OutChannel.desc());
	    return "pay/payConfigCompany/payConfigCompany";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("pay:payConfigCompany:payConfigCompany")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PayConfigCompanyDO> payConfigCompanyList = payConfigCompanyService.list(query);
		int total = payConfigCompanyService.count(query);
		PageUtils pageUtils = new PageUtils(payConfigCompanyList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("pay:payConfigCompany:add")
	String add(Model model){
		model.addAttribute("payCompanys", PayCompany.desc());
		model.addAttribute("outChannels", OutChannel.desc());
	    return "pay/payConfigCompany/add";
	}

	@GetMapping("/edit/{company}/{payMerch}/{outChannel}")
	@RequiresPermissions("pay:payConfigCompany:edit")
	String edit(@PathVariable("company") String company, @PathVariable("payMerch") String payMerch, @PathVariable("outChannel") String outChannel, Model model){
		PayConfigCompanyDO payConfigCompany = payConfigCompanyService.get(company,payMerch,outChannel);
		payConfigCompany.setPayPeriod(DateUtil.intFormatToTime(payConfigCompany.getPayPeriod()));
		model.addAttribute("payCompanys", PayCompany.desc());
		model.addAttribute("outChannels", OutChannel.desc());
		model.addAttribute("payConfigCompany", payConfigCompany);
	    return "pay/payConfigCompany/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("pay:payConfigCompany:add")
	public R save( PayConfigCompanyDO payConfigCompany){
		if(payConfigCompanyService.save(payConfigCompany) != null){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("pay:payConfigCompany:edit")
	public R update(PayConfigCompanyDO payConfigCompany){
		payConfigCompanyService.update(payConfigCompany);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("pay:payConfigCompany:remove")
	public R remove(@RequestParam("company") String company, @RequestParam("payMerch") String payMerch, @RequestParam("outChannel") String outChannel){
		if(payConfigCompanyService.remove(company,payMerch,outChannel)>0){
		return R.ok();
		}
		return R.error();
	}
	
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("pay:payConfigCompany:batchRemove")
	public R batchRemove(@RequestParam("companys") String[] companys, @RequestParam("payMerchs") String[] payMerchs, @RequestParam("outChannels") String[] outChannels){
		if(payConfigCompanyService.batchRemove(companys,payMerchs,outChannels)>0){
			return R.ok();
		}
		return R.error();
	}
}
