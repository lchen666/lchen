package com.lchen.common.swagger.dto;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.config.EnablePluginRegistries;

@Configuration
@ComponentScan(basePackages = {
        "com.lchen.common.swagger.dto",
})
@EnablePluginRegistries({
        ApiJsonObjectPlugin.class,
        ApiJsonPropertyPlugin.class,
        ApiJsonRefModelReplacePlugin.class
})
public class ApiJsonDocumentationConfiguration {

    @Bean
    public ApiJsonClassLoader apiJsonClassLoader(){
        return new ApiJsonClassLoader();
    }
}
