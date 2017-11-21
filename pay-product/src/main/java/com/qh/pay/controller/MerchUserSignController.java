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

import com.qh.pay.domain.MerchUserSignDO;
import com.qh.pay.service.MerchUserSignService;
import com.qh.common.utils.PageUtils;
import com.qh.common.utils.Query;
import com.qh.common.utils.R;

/**
 * 商户号下的用户签约信息
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-02 11:21:44
 */
 
@Controller
@RequestMapping("/pay/merchUserSign")
public class MerchUserSignController {
	@Autowired
	private MerchUserSignService merchUserSignService;
	
	@GetMapping()
	@RequiresPermissions("pay:merchUserSign:merchUserSign")
	String MerchUserSign(){
	    return "pay/merchUserSign/merchUserSign";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("pay:merchUserSign:merchUserSign")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<MerchUserSignDO> merchUserSignList = merchUserSignService.list(query);
		int total = merchUserSignService.count(query);
		PageUtils pageUtils = new PageUtils(merchUserSignList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("pay:merchUserSign:add")
	String add(){
	    return "pay/merchUserSign/add";
	}

	@GetMapping("/edit/{merchNo}")
	@RequiresPermissions("pay:merchUserSign:edit")
	String edit(@PathVariable("merchNo") String merchNo,Model model){
		MerchUserSignDO merchUserSign = merchUserSignService.get(merchNo);
		model.addAttribute("merchUserSign", merchUserSign);
	    return "pay/merchUserSign/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("pay:merchUserSign:add")
	public R save( MerchUserSignDO merchUserSign){
		if(merchUserSignService.save(merchUserSign)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("pay:merchUserSign:edit")
	public R update( MerchUserSignDO merchUserSign){
		merchUserSignService.update(merchUserSign);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("pay:merchUserSign:remove")
	public R remove( String merchNo){
		if(merchUserSignService.remove(merchNo)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("pay:merchUserSign:batchRemove")
	public R remove(@RequestParam("ids[]") String[] merchNos){
		merchUserSignService.batchRemove(merchNos);
		return R.ok();
	}
	
}
