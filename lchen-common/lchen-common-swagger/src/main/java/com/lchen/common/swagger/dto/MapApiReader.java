package com.lchen.common.swagger.dto;

import com.google.common.base.Optional;
import com.lchen.common.swagger.annotation.ApiJsonObject;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order
public class MapApiReader implements ParameterBuilderPlugin {

    @Override
    public void apply(ParameterContext parameterContext) {
        String requestMappingPatternName = parameterContext.getOperationContext().requestMappingPattern();
        Optional<ApiJsonObject> annotation = parameterContext.getOperationContext().findAnnotation(ApiJsonObject.class);
        if (annotation.isPresent()) {
            String name = SwaggerASMUtil.returnClassName(requestMappingPatternName, "H" + annotation.get().name());
            parameterContext.parameterBuilder().parameterType("body").modelRef(new ModelRef(name)).name(name);
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
