package com.lchen;

import com.lchen.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCustomSwagger2
@EnableFeignClients
@MapperScan("com.lchen.mapper")
public class LchenUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(LchenUserApplication.class, args);
    }

}
