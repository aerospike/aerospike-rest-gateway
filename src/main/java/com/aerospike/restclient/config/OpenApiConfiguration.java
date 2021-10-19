package com.aerospike.restclient.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Aerospike REST Client")
                .description("REST Interface for Aerospike Database.")
                .version("2.0")
                .contact(apiContact())
                .license(apiLicence());
    }

    private License apiLicence() {
        return new License()
                .name("Apache 2.0 License")
                .url("http://www.apache.org/licenses/LICENSE-2.0");
    }

    private Contact apiContact() {
        return new Contact()
                .name("Aerospike, Inc.")
                .url("https://www.aerospike.com");
    }
}
