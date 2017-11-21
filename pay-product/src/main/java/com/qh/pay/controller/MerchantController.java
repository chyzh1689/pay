package com.qh.pay.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qh.common.config.Constant;
import com.qh.common.utils.PageUtils;
import com.qh.common.utils.Query;
import com.qh.common.utils.R;
import com.qh.pay.api.constenum.OutChannel;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.pay.domain.Merchant;
import com.qh.pay.service.MerchantService;

/**
 * 启晗商户
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-01 10:05:41
 */
 
@Controller
@RequestMapping("/pay/merchant")
public class MerchantController {
	@Autowired
	private MerchantService merchantService;
	
	@GetMapping()
	@RequiresPermissions("pay:merchant:merchant")
	String Merchant(Model model){
		model.addAttribute("outChannels", OutChannel.desc());
	    return "pay/merchant/merchant";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("pay:merchant:merchant")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Merchant> merchantList = merchantService.list(query);
		int total = merchantService.count(query);
		PageUtils pageUtils = new PageUtils(merchantList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("pay:merchant:add")
	String add(Model model){
		model.addAttribute("merchNo", merchantService.defaultMerchantNo());
		model.addAttribute("outChannels", OutChannel.desc());
	    return "pay/merchant/add";
	}

	@GetMapping("/edit/{merchNo}")
	@RequiresPermissions("pay:merchant:edit")
	String edit(@PathVariable("merchNo") String merchNo,Model model){
		Merchant merchant = merchantService.get(merchNo);
		model.addAttribute("merchant", merchant);
		if(ParamUtil.isNotEmpty(merchant.getFeeRate())){
			model.addAttribute("feeRates",JSON.parseObject(merchant.getFeeRate()));
		}else{
			model.addAttribute("feeRates",new JSONObject());
		}
		model.addAttribute("outChannels", OutChannel.desc());
	    return "pay/merchant/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("pay:merchant:add")
	public R save( Merchant merchant){
		int count = merchantService.save(merchant);
		if(count == 1 ){
			return R.ok();
		}else if(count == Constant.data_exist){
			return R.error(merchant.getMerchNo() + "商户已经存在");
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("pay:merchant:edit")
	public R update(Merchant merchant){
		merchantService.update(merchant);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("pay:merchant:remove")
	public R remove(String merchNo){
		if(merchantService.remove(merchNo)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("pay:merchant:batchRemove")
	public R remove(@RequestParam("merchNos[]") String[] merchNos){
		merchantService.batchRemove(merchNos);
		return R.ok();
	}
	
	@PostMapping("/exist")
	@ResponseBody
	boolean exist(@RequestParam("merchNo") String merchNo) {
		// 存在，不通过，false
		return !merchantService.exist(merchNo);
	}
}
