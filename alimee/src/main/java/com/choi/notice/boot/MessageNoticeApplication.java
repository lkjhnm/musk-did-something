package com.choi.notice.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = BootConfiguration.class)
public class MessageNoticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageNoticeApplication.class, args);
	}

}
