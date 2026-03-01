package com.soat.fiap.videocore.reports.core.domain.exceptions;

/**
 * Exceção lançada quando um reporte não é encontrado
 */
public class ReportNotFoundException extends RuntimeException {

	public ReportNotFoundException(String message) {
		super(message);
	}

	public ReportNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportNotFoundException(String message, Object... args) {
		super(String.format(message, args));
	}
}
