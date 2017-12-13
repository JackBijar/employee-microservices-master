package com.spring.cloud.service.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.spring.cloud.service.gateway.filter.ErrorFilter;
import com.spring.cloud.service.gateway.filter.PostFilter;
import com.spring.cloud.service.gateway.filter.PreFilter;
import com.spring.cloud.service.gateway.filter.RouteFilter;
import com.spring.cloud.service.gateway.security.JwtUserDetailsServiceImpl;
import com.spring.cloud.service.gateway.security.User;

import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
//@EnableSwagger2
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}
	
	@Bean
	UiConfiguration uiConfig() {
		return new UiConfiguration("validatorUrl", "list", "alpha", "schema",
				UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
	}
	
	@Bean
	public JwtUserDetailsServiceImpl jwtUserDetailsServiceImpl()
	{
		return new JwtUserDetailsServiceImpl();
	}
	
	@Bean
	public User modelUser()
	{
		return new User();
	}
	
	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}
	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}
	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}
	@Bean
	public RouteFilter routeFilter() {
		return new RouteFilter();
	}
}
