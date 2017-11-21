package com.qh.pay.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.qh.pay.domain.Merchant;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.qh.pay")
public class MerchantServiceTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MerchantServiceTest.class);


    @Autowired
    private MerchantService merchantService;

    @Test
    public void insertMerchant(){
        Merchant merchant = new Merchant();
        merchant.setMerchNo("chdx");
        merchant.setName("陈大侠");
        merchant.setBalance(new BigDecimal("0"));
        int count = merchantService.save(merchant);
        LOGGER.info("count:{},id:{}",count, merchant.getUserId());
    }






}