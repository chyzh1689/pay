package com.qh.pay.service;

import com.qh.pay.domain.MerchUserSignDO;

import java.util.List;
import java.util.Map;

/**
 * 商户号下的用户签约信息
 * 
 * @author chyzh
 * @email 3048427407@qq.com
 * @date 2017-11-02 11:21:44
 */
public interface MerchUserSignService {
	
	MerchUserSignDO get(String merchNo);
	
	List<MerchUserSignDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MerchUserSignDO merchUserSign);
	
	int update(MerchUserSignDO merchUserSign);
	
	int remove(String merchNo);
	
	int batchRemove(String[] merchNos);
}
