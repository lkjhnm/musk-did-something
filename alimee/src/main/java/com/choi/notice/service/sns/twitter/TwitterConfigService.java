package com.choi.notice.service.sns.twitter;

import com.choi.notice.boot.config.BootConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TwitterConfigService {

	BootConfiguration.ApiConfig apiConfig;

	private String bearToken;

	@PostConstruct
	public void initialize() {
		this.bearToken = this.apiConfig.getProperty("twitter.api.bearer.token");
	}

	@Autowired
	public void setApiConfig(BootConfiguration.ApiConfig apiConfig) {
		this.apiConfig = apiConfig;
	}

	public String getTwitterToken() {
		return this.bearToken;
	}
}
