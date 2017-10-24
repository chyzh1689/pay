package com.qh.pay;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDiscoveryClient//激活Eureka中的DiscoveryClient实现(自动化配置,创建DiscoveryClient接口针对Eureka客户端的EurekaDiscoveryClient实例)
@SpringBootApplication
public class PayProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayProductApplication.class, args);
        System.out.println("sss");
    }
}