package com.lchen.common.swagger.dto;


import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;

public interface ApiJsonPropertyPlugin extends Plugin<DocumentationType> {

    void apply(ApiJsonDocumentationContext context);

}
