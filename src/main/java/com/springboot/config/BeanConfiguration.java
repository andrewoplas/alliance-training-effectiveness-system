package com.springboot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import com.springboot.interceptor.AdminInterceptor;
import com.springboot.interceptor.AuthInterceptor;
import com.springboot.interceptor.LoginInterceptor;

@Configuration
@ComponentScan({ "com.springboot.controller", "com.springboot.service", "com.springboot.repository.custom" })
@EntityScan("com.springboot.entities")
@EnableJpaRepositories({ "com.springboot.repository", "com.springboot.repository.custom" })
public class BeanConfiguration extends WebMvcConfigurerAdapter {


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/ates/**");
		registry.addInterceptor(new AuthInterceptor())
			.addPathPatterns("/login")
			.addPathPatterns("/register");
		
		registry.addInterceptor(new AdminInterceptor())
			.addPathPatterns("/ates/users/**")
			.addPathPatterns("/ates/training/**")
			.addPathPatterns("/ates/form/**")
			.addPathPatterns("/ates/forms/**")
			.addPathPatterns("/ates/dashboard");
		
	}

	@Bean
	public UrlBasedViewResolver viewResolver() {
		UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
		tilesViewResolver.setViewClass(TilesView.class);
		return tilesViewResolver;
	}

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tiles = new TilesConfigurer();
		tiles.setDefinitions(new String[] { "/WEB-INF/tiles/tiles.xml" });
		return tiles;

	}
}
