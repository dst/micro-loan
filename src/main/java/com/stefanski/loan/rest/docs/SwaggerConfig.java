package com.stefanski.loan.rest.docs;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dariusz Stefanski
 */
@Configuration
@EnableSwagger
public class SwaggerConfig {

    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;

    @Value("${info.app.version}")
    private String version;

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(apiInfo())
                .apiVersion(version)
                .includePatterns("/loans.*", "/customers.*", "/info");
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "MICRO-LOAN",
                "RESTful web service for micro loans",
                "http://www.keepcalm-o-matic.co.uk/p/keep-calm-and-do-whatever-you-want-4",
                "you-can-find-yourself@example.com",
                "Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html"
        );
    }
}
