package com.qh.pay.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qh.common.exception.BDException;
import com.qh.common.utils.ShiroUtils;
import com.qh.pay.api.constenum.AuditResult;
import com.qh.pay.api.utils.DateUtil;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.pay.dao.PayAuditDao;
import com.qh.pay.domain.PayAuditDO;
import com.qh.pay.service.PayAuditService;



@Service
public class PayAuditServiceImpl implements PayAuditService {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PayAuditServiceImpl.class);
	@Autowired
	private PayAuditDao payAuditDao;
	
	@Override
	public PayAuditDO get(String orderNo, String merchNo, Integer auditType){
		return payAuditDao.get(orderNo, merchNo, auditType);
	}
	
	@Override
	public List<PayAuditDO> list(Map<String, Object> map){
		map.put("beginDate", DateUtil.getBeginTimeIntZero((Date) map.get("beginDate")));
		map.put("endDate", DateUtil.getEndTimeIntLast((Date) map.get("endDate")));
		return payAuditDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return payAuditDao.count(map);
	}

	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.PayAuditService#audit(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional
	public int audit(String orderNo, String merchNo, Integer auditType, Integer auditResult) {
		String username = ShiroUtils.getUsername();
		return singleAudit(orderNo, merchNo, auditType, auditResult,username);
	}

	
	private int singleAudit(String orderNo, String merchNo, Integer auditType, Integer auditResult,String username){
		if(ParamUtil.isNotEmpty(orderNo) && ParamUtil.isNotEmpty(merchNo) && auditType != null && auditResult != null){
			PayAuditDO payAudit = payAuditDao.get(orderNo, merchNo, auditType);
			if(payAudit == null || payAudit.getAuditResult()!= AuditResult.init.id()){
				return 0;
			}
			payAudit.setAuditResult(auditResult);
			payAudit.setAuditTime(new Date());
			payAudit.setAuditor(username);
			payAudit.setMemo(username + (auditResult == 1?"审核通过":"审核不通过"));
			return payAuditDao.update(payAudit);
		}
		return 0;
	}
	
	/* (非 Javadoc)
	 * Description:
	 * @see com.qh.pay.service.PayAuditService#batchAudit(java.lang.String[], java.lang.String[], java.lang.Integer[], java.lang.Integer)
	 */
	@Override
	@Transactional
	public int batchAudit(String[] orderNos, String[] merchNos, Integer[] auditTypes, Integer auditResult) {
		String username = ShiroUtils.getUsername();
		int count = 0;
		int len = orderNos.length;
		for (int i = 0; i < len; i++) {
			count += singleAudit(orderNos[i], merchNos[i], auditTypes[i], auditResult, username);
		}
		if(count < len){
			String msg = "本次批量审核失败,审核数量：" + len + ",实际审核数量：" + count;
			logger.info(msg);
			throw new BDException(msg);
		}
		return count;
	}
	

	
}
