package aspirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AspirestApplication {

	public static void main(String[] args) {
		System.setProperty("springdoc.api-docs.path", "/v3/api-docs");
		System.setProperty("springdoc.swagger-ui.path", "/swagger-ui.html");
		System.setProperty("springdoc.swagger-ui.tags-sorter", "alpha");
		System.setProperty("springdoc.swagger-ui.operations-sorter", "method");
		SpringApplication.run(AspirestApplication.class, args);
	}

	

}
