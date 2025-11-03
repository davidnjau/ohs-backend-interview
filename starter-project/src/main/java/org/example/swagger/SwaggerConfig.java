package org.example.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI vroadcastOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OHS API")
                        .description("API documentation for the ohs system.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("David Njau")
                                .email("davidnjau21@gmail.com")
                                .url("https://davidnjau21.com"))
                        .license(new License()
                                .name("Proprietary - Internal Use Only")
                                .url("https://davidnjau21.com/licensing")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Dev Server"),
                        new Server().url("").description("Production Server")
                ));
    }
}
