package com.soat.fiap.videocore.reports.infrastructure.in.http.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do SpringDoc para documentação da API
 */
@Configuration
public class SpringDocConfig {

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Video Core API - Reports")
						.description(
								"Microsserviço para gerenciamento dos reportes de atualização do status de processamento de um vídeo, implementado com Clean Architecture e DDD.")
						.version("1.0.0")
						.contact(new Contact().name("Equipe FIAP/SOAT")
								.email("suporte@videocoreapi.com")
								.url("https://github.com/FIAP-SOAT-TECH-TEAM/videocore-reports"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
				.servers(List.of(new Server().url(contextPath).description("API Server")));
	}

}
