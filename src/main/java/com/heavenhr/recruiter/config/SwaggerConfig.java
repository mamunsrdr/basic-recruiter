package com.heavenhr.recruiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(globalOperationParams())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.heavenhr.recruiter.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Basic recruiter REST API",
                "Basic recruiter REST API documentations",
                "V1",
                "Terms of service",
                new Contact("Mamun Sardar", "https://mamunsardar.com", "mamun.srdr@gmail.com"),
                "Open License", "N/A", Collections.emptyList());
    }

    private List<Parameter> globalOperationParams() {
        return Collections.singletonList(new ParameterBuilder()
                .name("Accept-Language")
                .description("Language header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
    }
}
