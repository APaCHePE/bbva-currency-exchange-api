package com.bbva.exchange.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("Exchange Rate API")
        .version("1.0.0")
        .description("API para el sistema de tasas de cambio")
        .contact(new Contact()
          .name("Alex P.")
          .email("APaCHePE@gmail.com")
          .url("https://github.com/APaCHePE"))
        .license(new License()
          .name("Licencia GITHUB")
          .url("https://github.com/APaCHePE")))
      .externalDocs(new ExternalDocumentation()
        .description("Documentación adicional")
        .url("https://github.com/APaCHePE/bbva-currency-exchange-infra/blob/main/README.md"))
      .addServersItem(new Server().url("http://localhost:4040"));
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
      .group("exchange-api")
      .pathsToMatch("/**")
      .build();
  }
}