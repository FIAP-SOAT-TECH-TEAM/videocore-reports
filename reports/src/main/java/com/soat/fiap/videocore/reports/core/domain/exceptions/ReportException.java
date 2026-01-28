package com.soat.fiap.videocore.reports.core.domain.exceptions;

/**
 * Exceção lançada quando uma regra de negócio é violada referente ao reporte de atualização no status de processamento do vídeo
 */
public class ReportException extends RuntimeException {

	public ReportException(String message) {
		super(message);
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}
}