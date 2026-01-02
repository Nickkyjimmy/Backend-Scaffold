package com.rmit.generic.generic_service.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration
 * Provides API documentation at /swagger-ui.html
 */
@Configuration
@SecurityScheme(name = "bearer-auth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@OpenAPIDefinition(
        info = @Info(
                title = "Generic Service API",
                version = "1.0.0",
                description = "Generic microservice API documentation with full CRUD operations",
                contact = @Contact(
                        name = "RMIT",
                        email = "support@rmit.edu.au"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "/api/${spring.application.name}", description = "Gateway URL"),
                @Server(url = "http://localhost:${server.port}", description = "Direct Service URL")
        }
)
public class OpenApiConfig {
}

