package com.lchen.common.swagger.dto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiJsonObjectScanner {
    //private static final Logger LOG = LoggerFactory.getLogger(ApiJsonObjectScanner.class);

    private final ApiJsonPluginManager apiJsonPluginManager;

    @Autowired
    public ApiJsonObjectScanner(ApiJsonPluginManager apiJsonPluginManager){
        this.apiJsonPluginManager = apiJsonPluginManager;
    }

    public ApiJsonPluginManager getApiJsonPluginManager() {
        return apiJsonPluginManager;
    }

    public void read(ApiJsonDocumentationContext context){
        apiJsonPluginManager.apiJsonObject(context);
    }
}
