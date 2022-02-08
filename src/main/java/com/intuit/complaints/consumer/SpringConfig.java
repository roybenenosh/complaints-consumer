package com.intuit.complaints.consumer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.intuit.complaints.consumer")
public class SpringConfig {
}