package com.codecool.bankaccountstarter.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Bank Account Starter API",
                version = "2.0",
                description = "Basic interactions for account transactions"
        )
)
@Configuration
public class SwaggerConfig {
}
