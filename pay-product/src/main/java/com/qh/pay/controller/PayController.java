package com.qh.pay.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.qh.pay.entity.Merchant;
import com.qh.pay.service.MerchantService;

/**
 * 
 * @ClassName PayController
 * @Description pay
 * @author chenyuezhi
 * @Date 2017年10月24日 上午11:30:22
 * @version 1.0.0
 */
@RestController
public class PayController {

    @Autowired
    private MerchantService merchantService;

    @GetMapping("/index")
    public String index(){
        return "helloworld!";
    }
    
    @GetMapping("/merchant/{id}")
    public Merchant findById(@PathVariable Long id){
        return merchantService.findMerchantById(id);
    }
}