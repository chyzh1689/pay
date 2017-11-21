package com.qh.pay.service;

import com.qh.pay.domain.PayConfigCompanyDO;

import java.util.List;
import java.util.Map;

/**
 * 支付公司配置
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-06 16:00:33
 */
public interface PayConfigCompanyService {
	
	PayConfigCompanyDO get(String company,String payMerch,String outChannel);
	
	List<PayConfigCompanyDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	PayConfigCompanyDO save(PayConfigCompanyDO payConfigCompany);
	
	PayConfigCompanyDO update(PayConfigCompanyDO payConfigCompany);
	
	int remove(String company,String payMerch,String outChannel);

	/**
	 * @Description 批量删除
	 * @param companys
	 * @param payMerchs
	 * @param outChannels
	 * @return
	 */
	int batchRemove(String[] companys, String[] payMerchs, String[] outChannels);
	
	
	List<Object> getPayCfgCompByOutChannel(String outChannel);
	
}
