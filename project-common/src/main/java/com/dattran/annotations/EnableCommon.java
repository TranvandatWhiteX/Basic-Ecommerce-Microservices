package com.dattran.annotations;

import com.dattran.configs.CommonConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CommonConfig.class)
public @interface EnableCommon {
}
