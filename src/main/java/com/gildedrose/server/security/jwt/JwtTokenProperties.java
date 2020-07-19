package com.gildedrose.server.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProperties {
	private String tokenSecret;
	private long tokenExpirationMsec;
}
