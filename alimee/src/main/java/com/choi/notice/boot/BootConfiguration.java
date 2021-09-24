package com.choi.notice.boot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 서비스의 Spring 설정을 관리하는 클래스
 */
@Configuration
@ComponentScan(basePackages = {"com.choi.notice"})
public class BootConfiguration {
}
