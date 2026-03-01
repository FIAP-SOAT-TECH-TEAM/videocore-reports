package com.soat.fiap.videocore.reports.core.domain.exceptions;

/**
 * Exceção lançada quando não é possível identificar a autenticação de um
 * usuário
 */
public class NotAuthorizedException extends RuntimeException {

	public NotAuthorizedException(String message) {
		super(message);
	}

	public NotAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}
}
