package com.lchen.common.swagger.dto;

import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;

public interface ApiJsonRefModelReplacePlugin extends Plugin<DocumentationType>, PluginOrder {

    void apply(ApiJsonRefContext context);

}
