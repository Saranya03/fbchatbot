package com.xlabs.insuretech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class InsuretechApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuretechApplication.class, args);
	}

	// Swagger ui configuration
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.xlabs.insuretech"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Facebook Caht",
				"Smart APIs for Smarter Banking",
				"V2.0",
				"Terms of service",
				"pwelagedara@virtusapolaris.com",
				"Virtusa|Polaris",
				"http://www.virtusapolaris.com");
		return apiInfo;
	}
}
