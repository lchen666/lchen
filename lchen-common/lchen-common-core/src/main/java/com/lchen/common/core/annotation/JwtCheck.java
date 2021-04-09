package com.lchen.common.core.annotation;

import java.lang.annotation.*;

/**
 * 描述: jwt检查注解
 *
 * @Auther: lchen
 * @Date:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JwtCheck {

    String value() default "";
}
