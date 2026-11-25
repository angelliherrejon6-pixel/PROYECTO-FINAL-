package aspirest.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {
      @Bean
    public OpenAPI aspirestOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Aspirest - Servicio de música")
                .version("0.0.1")
                .description("API REST para gestionar usuarios, listas de reproducción y el catálogo de medios.")
                .contact(new Contact().name("Equipo Aspirest")));
    }
}
