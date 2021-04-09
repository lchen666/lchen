package com.lchen.common.swagger.dto;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

public class ApiJsonRefContextBuilder {

    private final RequestMappingContext requestMappingContext;
    private final ApiJsonDocumentation documentation;
    private final TypeResolver typeResolver;

    public ApiJsonRefContextBuilder(RequestMappingContext requestMappingContext,
                                    ApiJsonDocumentation documentation, TypeResolver typeResolver){
        this.requestMappingContext = requestMappingContext;
        this.documentation = documentation;
        this.typeResolver = typeResolver;
    }

    public ApiJsonRefContext build(){
        return new ApiJsonRefContext(requestMappingContext,documentation,typeResolver);
    }
}
