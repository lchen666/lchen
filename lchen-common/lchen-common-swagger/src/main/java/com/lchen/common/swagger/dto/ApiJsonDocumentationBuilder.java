package com.lchen.common.swagger.dto;



import java.util.List;

public class ApiJsonDocumentationBuilder {

    private String name;
    private List<ApiJsonPropertySingle> properties;

    public ApiJsonDocumentationBuilder name(String name){
        this.name = name;
        return this;
    }

    public ApiJsonDocumentationBuilder properties(List<ApiJsonPropertySingle> properties){
        this.properties = properties;
        return this;
    }

    public ApiJsonDocumentation build(){
        return new ApiJsonDocumentation(
                name,
                properties
        );
    }
}
