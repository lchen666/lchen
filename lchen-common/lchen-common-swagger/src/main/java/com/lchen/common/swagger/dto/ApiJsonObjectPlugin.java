package com.lchen.common.swagger.dto;


import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;

public interface ApiJsonObjectPlugin extends Plugin<DocumentationType> {

    void apply(ApiJsonDocumentationContext context);

}
