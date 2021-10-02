package com.choi.notice.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.choi.notice.boot.config"})
public class MessageNoticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageNoticeApplication.class, args);
	}

}
