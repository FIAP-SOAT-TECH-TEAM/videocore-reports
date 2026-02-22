package com.soat.fiap.videocore.reports.infrastructure.in.http.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.soat.fiap.videocore.reports.infrastructure.common.exceptions.http.CorsAllowedOriginEmptyException;

/**
 * Configuração global de CORS para a aplicação (Endpoints HTTP).
 *
 * <p>
 * As origens permitidas são definidas dinamicamente pela propriedade
 * {@code http.cors.allowed-origins}, que aceita uma lista separada por vírgulas
 * ou o valor curinga ({@code *}).
 * </p>
 *
 * <p>
 * Todos os headers e métodos HTTP são permitidos. O uso de credenciais
 * ({@code allowCredentials}) é habilitado apenas quando não há curinga
 * ({@code *}) entre as origens configuradas, em conformidade com as restrições
 * do Spring e da especificação CORS.
 * </p>
 */
@Configuration
public class CorsConfig {

	@Value("${http.cors.allowed-origins}")
	private String allowedOrigins;

	/**
	 * Cria e registra um {@link CorsFilter} aplicável a todas as rotas da
	 * aplicação.
	 *
	 * <p>
	 * A configuração falha explicitamente durante a inicialização caso a
	 * propriedade {@code http.cors.allowed-origins} esteja ausente, em branco ou
	 * resulte em uma lista vazia de origens válidas.
	 * </p>
	 *
	 * @return filtro de CORS configurado conforme as origens definidas em
	 *         {@code http.cors.allowed-origins}
	 */
	@Bean
	public CorsFilter corsFilter() {
		if (allowedOrigins == null || allowedOrigins.isBlank())
			throw new CorsAllowedOriginEmptyException(
					"Allowed Origins não pode estar em branco durante a configuração de CORS");

		var config = new CorsConfiguration();
		var source = new UrlBasedCorsConfigurationSource();
		var origins = Arrays.stream(allowedOrigins.split(","))
				.map(String::trim)
				.filter(origin -> !origin.isEmpty())
				.toList();

		if (origins.isEmpty())
			throw new CorsAllowedOriginEmptyException(
					"Allowed Origins não pode estar em branco durante a configuração de CORS");
		var hasWildcard = origins.contains("*");

		config.setAllowedOriginPatterns(origins);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setAllowCredentials(!hasWildcard);

		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
