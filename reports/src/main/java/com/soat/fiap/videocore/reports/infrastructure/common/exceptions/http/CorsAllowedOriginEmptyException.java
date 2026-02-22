package com.soat.fiap.videocore.reports.infrastructure.common.exceptions.http;

/**
 * Exceção lançada quando o valor da propriedade {@code @Value("${http.cors.allowed-origins}") } é vazia
 */
public class CorsAllowedOriginEmptyException extends RuntimeException {

	public CorsAllowedOriginEmptyException(String message) {
		super(message);
	}

	public CorsAllowedOriginEmptyException(String message, Throwable cause) {
		super(message, cause);
	}
}
