package com.vikas.onlineShopping.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
	    Path brandUploadDir=Paths.get("./brand-logos");
		
		System.out.println("brandUploadDir  : "+brandUploadDir);
		
		String brandUploadPath=brandUploadDir.toFile().getAbsolutePath();
		
		System.out.println("brandUploadPath  : "+brandUploadPath);
		
		registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:/" + brandUploadPath + "/");
	}

}
