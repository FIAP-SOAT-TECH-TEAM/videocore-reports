package com.soat.fiap.videocore.reports.infrastructure.common.exceptions.persistence;

/**
 * Exceção lançada quando um parâmetro de ordenação de registros é inválido
 */
public class OrderParamException extends RuntimeException {

	public OrderParamException(String message) {
		super(message);
	}

	public OrderParamException(String message, Throwable cause) {
		super(message, cause);
	}
}
