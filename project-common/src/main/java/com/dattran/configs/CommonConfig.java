package com.dattran.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.dattran")
@EnableAspectJAutoProxy
public class CommonConfig {
}
