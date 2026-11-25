package aspirest.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {
      @Bean
    public OpenAPI aspirestOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Aspirest - Servicio de música")
                .version("0.0.1")
                        .description("API REST para gestionar usuarios, listas de reproducción y el catálogo de medios. "
                                + "Para probar los endpoints protegidos: inicia sesión en /api/auth/login, ")
                        .contact(new Contact().name("Equipo Aspirest")))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
