package com.soat.fiap.videocore.reports.core.domain.exceptions;

/**
 * Exceção lançada quando um usuário não está autorizado a realizar uma ação
 */
public class ForbiddenException extends RuntimeException {

	public ForbiddenException(String message) {
		super(message);
	}

	public ForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}
}
