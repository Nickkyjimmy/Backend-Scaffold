package com.rmit.destination.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Destination Service API",
                version = "1.0.0",
                description = "Destination Service API Documentation",
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
public class SwaggerConfig {
}

