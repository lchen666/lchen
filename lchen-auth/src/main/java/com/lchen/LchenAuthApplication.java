package com.lchen;

import com.lchen.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权中心
 *
 * @author lchen
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//自动装配去掉数据源配置
@EnableFeignClients
@EnableCustomSwagger2
public class LchenAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(LchenAuthApplication.class, args);
    }

}
