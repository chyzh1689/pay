package com.qh.pay.service;
import java.util.List;

import com.qh.pay.entity.Merchant;

/**
 * 
 * @ClassName MerchantService
 * @Description 商户操作类
 * @author chenyuezhi
 * @Date 2017年10月24日 上午9:27:47
 * @version 1.0.0
 */
public interface MerchantService {

    Merchant findMerchantById(Long id);

    List<Merchant> findAllMerchants();

    int insertMerchant(Merchant merchant);
}