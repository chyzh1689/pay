package com.qh.pay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qh.pay.entity.Merchant;
import com.qh.pay.mapper.MerchantMapper;
import com.qh.pay.service.MerchantService;

/**
 * @ClassName MerchantServiceImpl
 * @Description 商户实现类
 * @author chenyuezhi
 * @Date 2017年10月24日 上午9:28:10
 * @version 1.0.0
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public Merchant findMerchantById(Long id) {
        return merchantMapper.findMerchantById(id);
    }

    @Override
    public List<Merchant> findAllMerchants() {
        return merchantMapper.findAllMerchants();
    }

    @Override
    public int insertMerchant(Merchant merchant) {
        return merchantMapper.insertMerchant(merchant);
    }

}
