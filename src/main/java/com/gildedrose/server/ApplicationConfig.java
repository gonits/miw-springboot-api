package com.gildedrose.server;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gildedrose.server.domain.SurgePrice;
import com.gildedrose.server.domain.SurgePriceImpl;

import lombok.Data;

/**
 * This is a configuration class to configure surgeprice bean with externalised
 * configurations. viewcounts,surgeduration and surgeIncrementPercentage are all
 * externalized coming from application.properties file.
 * 
 * @author Nitika Goel
 *
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {

	private int viewcounts;
	private Duration surgeDuration;
	private double surgeIncrementPercentage;

	@Bean
	public SurgePrice surgePriceImpl() {
		return new SurgePriceImpl(surgeDuration, viewcounts, surgeIncrementPercentage);
	}
}
