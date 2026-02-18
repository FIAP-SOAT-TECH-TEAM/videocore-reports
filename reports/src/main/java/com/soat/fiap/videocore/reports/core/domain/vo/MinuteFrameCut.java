package com.soat.fiap.videocore.reports.core.domain.vo;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;

/**
 * Objeto de valor que representa o tempo de corte de cada frame para captura de
 * imagens em minutos.
 */
public record MinuteFrameCut(long value) {

	public MinuteFrameCut {
		validate(value);
	}

	private static void validate(long value) {
		if (value <= 0) {
			throw new ReportException("O tempo de corte do frame deve ser maior que zero");
		}
	}
}
