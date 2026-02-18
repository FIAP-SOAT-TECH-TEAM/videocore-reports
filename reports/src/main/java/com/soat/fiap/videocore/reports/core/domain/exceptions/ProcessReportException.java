package com.soat.fiap.videocore.reports.core.domain.exceptions;

/**
 * Exceção lançada quando um erro ocorre no processamento do reporte de uma
 * atualização de status de processamento de um vídeo
 */
public class ProcessReportException extends RuntimeException {

	public ProcessReportException(String message) {
		super(message);
	}

	public ProcessReportException(String message, Throwable cause) {
		super(message, cause);
	}
}
