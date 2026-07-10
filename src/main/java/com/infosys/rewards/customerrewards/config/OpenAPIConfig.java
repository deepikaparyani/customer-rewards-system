package com.infosys.rewards.customerrewards.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Customer Rewards API",
                version = "1.0.0",
                description = "REST API for calculating customer reward points based on purchase transactions.",
                contact = @Contact(
                        name = "Deepika Paryani",
                        email = "deepika@example.com"
                ),
                license = @License(
                        name = "MIT License"
                )
        ),
        servers = {
                @Server(
                        description = "Local Server",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenAPIConfig {

}
