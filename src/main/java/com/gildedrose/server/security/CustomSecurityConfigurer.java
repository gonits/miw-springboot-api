package com.gildedrose.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gildedrose.server.security.jwt.JwtAuthenticationFilter;

/**
 * webSecurityConfigurer has been customized here to allow stateless session
 * management and manage which which calls need to be authenticated. A
 * JwtAuthenticationFilter has been initialized here which will intercept all
 * the authenticated calls to check for auth token. If auth token is invalid, it
 * will throw authentication exception.
 * 
 * @author Nitika Goel
 *
 */
@EnableWebSecurity
@Configuration
public class CustomSecurityConfigurer extends WebSecurityConfigurerAdapter {
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public JwtAuthenticationFilter authFilter;

	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint()).and().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/items/**").permitAll().antMatchers(HttpMethod.POST, "/auth/**")
				.permitAll().antMatchers(HttpMethod.POST, "/orders/**").authenticated().and()
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

	}

}
