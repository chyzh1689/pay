package com.qh;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.qh.*.dao")
@SpringBootApplication
public class PayProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayProductApplication.class, args);
    }
}