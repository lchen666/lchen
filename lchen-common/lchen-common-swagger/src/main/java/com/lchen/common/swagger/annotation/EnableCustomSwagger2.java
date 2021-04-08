package com.lchen.common.swagger.annotation;

import com.lchen.common.swagger.config.Swagger2;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ Swagger2.class })
public @interface EnableCustomSwagger2 {
}
