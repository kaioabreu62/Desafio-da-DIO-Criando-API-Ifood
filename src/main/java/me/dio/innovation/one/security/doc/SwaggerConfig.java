package me.dio.innovation.one.security.doc;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Title-Rest API")
                        .description("API exemplo de uso de Spring Boot REST API")
                        .version("1.0")
                        .termsOfService("Termo de uso: Open Source")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.seusite.com.br")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Seu nome")
                        .url("http://www.seusite.com.br"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/sessions/login", "/h2-console/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/total/**", "/payments/**", "/shoppingCart/**", "/foodOrders/**", "/users/create", "/users/update/{id}", "/users/delete/{id}")
                .pathsToExclude("/users/get/{id}", "/users/getAll")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info().title("User API").description("APIs para usuários autenticados").version("1.0"));
                })
                .build();
    }

    @Bean
    public GroupedOpenApi managerApi() {
        return GroupedOpenApi.builder()
                .group("manager")
                .pathsToMatch("/products/**", "/categories/**", "/users/create", "/users/getAll", "/users/get/{id}", "/users/update/{id}", "/users/delete/{id}")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info().title("Manager API").description("APIs para gerentes").version("1.0"));
                })
                .build();
    }
}
