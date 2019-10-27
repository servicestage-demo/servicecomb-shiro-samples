package com.service.servicecombshiro;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class ServiceCombShiroSimpleApplication {
    public static void main(String[] args) {
         SpringApplication.run(ServiceCombShiroSimpleApplication.class,args);
    }
}
