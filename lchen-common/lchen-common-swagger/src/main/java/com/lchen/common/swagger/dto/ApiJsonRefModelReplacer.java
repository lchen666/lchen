package com.lchen.common.swagger.dto;

import org.springframework.stereotype.Component;

@Component
public class ApiJsonRefModelReplacer {

    private final ApiJsonPluginManager apiJsonPluginManager;

    public ApiJsonRefModelReplacer(ApiJsonPluginManager apiJsonPluginManager){
        this.apiJsonPluginManager = apiJsonPluginManager;
    }

    public void replace (ApiJsonRefContext apiJsonRefContext){
        this.apiJsonPluginManager.apiJsonReplace(apiJsonRefContext);
    }
}
