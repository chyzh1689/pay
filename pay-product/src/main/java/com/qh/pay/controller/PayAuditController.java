package com.qh.pay.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qh.pay.api.constenum.AuditResult;
import com.qh.pay.api.constenum.AuditType;
import com.qh.pay.domain.PayAuditDO;
import com.qh.pay.service.PayAuditService;
import com.qh.redis.service.RedisMsg;
import com.qh.common.utils.PageUtils;
import com.qh.common.utils.Query;
import com.qh.common.utils.R;

/**
 * 支付审核
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-16 15:59:04
 */
 
@Controller
@RequestMapping("/pay/payAudit")
public class PayAuditController {
	@Autowired
	private PayAuditService payAuditService;
	
	@GetMapping()
	@RequiresPermissions("pay:payAudit:payAudit")
	String PayAudit(Model model){
		model.addAttribute("auditResults", AuditResult.desc());
		model.addAttribute("auditTypes", AuditType.desc());
	    return "pay/payAudit/payAudit";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("pay:payAudit:payAudit")
	public PageUtils list(@RequestParam("beginDate") Date beginDate, @RequestParam("endDate") Date endDate,@RequestParam Map<String, Object> params){
		// 查询列表数据
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
        Query query = new Query(params);
		List<PayAuditDO> payAuditList = payAuditService.list(query);
		int total = payAuditService.count(query);
		PageUtils pageUtils = new PageUtils(payAuditList, total);
		return pageUtils;
	}
	
	/**
	 * 审核
	 */
	@PostMapping( "/audit")
	@ResponseBody
	@RequiresPermissions("pay:payAudit:audit")
	public R audit(@RequestParam("orderNo") String orderNo, 
			@RequestParam("merchNo") String merchNo, @RequestParam("auditType") Integer auditType, @RequestParam("auditResult") Integer auditResult){
		if(payAuditService.audit(orderNo,merchNo,auditType,auditResult)>0){
			if(AuditResult.pass.id() == auditResult){
				RedisMsg.orderAcpMsg(merchNo, orderNo);
			}else{
				RedisMsg.orderAcpNopassMsg(merchNo, orderNo);
			}
			return R.ok();
		}
		return R.error("审核失败！");
	}
	
	/**
	 * 批量审核
	 */
	@PostMapping( "/batchAudit")
	@ResponseBody
	@RequiresPermissions("pay:payAudit:batchAudit")
	public R batchAudit(@RequestParam("orderNos[]") String[] orderNos, @RequestParam("merchNos[]") String[] merchNos, 
			@RequestParam("auditTypes[]") Integer[] auditTypes, @RequestParam("auditResult") Integer auditResult){
		try {
			if(payAuditService.batchAudit(orderNos,merchNos,auditTypes, auditResult) == orderNos.length){
				for (int len = orderNos.length ,i = 0; i < len; i++) {
					if(AuditResult.pass.id() == auditResult){
						RedisMsg.orderAcpMsg(merchNos[i], orderNos[i]);
					}else{
						RedisMsg.orderAcpNopassMsg(merchNos[i], orderNos[i]);
					}
				}
				return R.ok();
			};
		} catch (Exception e) {
			return R.error(e.getMessage());
		}
		
		return R.error("批量审核失败");
	}
}
