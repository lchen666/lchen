package com.lchen.common.swagger.dto;


import com.lchen.common.swagger.annotation.ApiJsonObject;
import com.lchen.common.swagger.annotation.ApiJsonProperty;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

@Component
public class ApiJsonPropertiesReader implements ApiJsonPropertyPlugin {

    @Override
    public void apply(ApiJsonDocumentationContext context) {
        List<ApiJsonPropertySingle> properties = newArrayList();

        ApiJsonObject annotations = context.getAnnotations();
        if(annotations != null){
            ApiJsonProperty[] values = annotations.value();
            if(values.length > 0){
                for (ApiJsonProperty v : values) {
                    properties.add(new ApiJsonPropertySingle(v.type(),v.key(),v.example(),v.description()));
                }
            }
        }
        context.setProperties(properties);
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }


}
