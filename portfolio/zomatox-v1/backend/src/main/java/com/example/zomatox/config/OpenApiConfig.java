package com.example.zomatox.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("ZomatoX v3-pro API")
        .version("v3-pro")
        .description("JWT + RBAC + coupons + address book + favorites/recent + admin ops"))
      .schemaRequirement("bearerAuth", new SecurityScheme()
        .name("bearerAuth")
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT"))
      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }
}
