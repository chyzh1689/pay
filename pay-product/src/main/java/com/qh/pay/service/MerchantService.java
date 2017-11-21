package com.qh.pay.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.qh.pay.api.constenum.UserType;
import com.qh.pay.domain.Merchant;
import com.qh.pay.domain.PayAcctBal;

/**
 * 启晗商户
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-01 10:05:41
 */
public interface MerchantService {
	
	Merchant get(String merchNo);
	
	List<Merchant> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(Merchant merchant);
	
	int update(Merchant merchant);
	
	int remove(String merchNos);
	
	int batchRemove(String[] merchNos);

	/**
	 * @Description 默认商户号
	 * @return
	 */
	String defaultMerchantNo();

	/**
	 * @Description 是否存在
	 * @param merchNo
	 * @return
	 */
	boolean exist(String merchNo);


	/**
	 * 
	 * @Description 创建支付账户余额
	 * @param merchant
	 * @return
	 */
	public static PayAcctBal createPayAcctBal(Merchant merchant){
		PayAcctBal payAcctBal = new PayAcctBal();
		payAcctBal.setUserId(merchant.getUserId());
		payAcctBal.setUsername(merchant.getMerchNo());
		payAcctBal.setUserType(UserType.merch.id());
		payAcctBal.setBalance(BigDecimal.ZERO);
		return payAcctBal;
	}
}
