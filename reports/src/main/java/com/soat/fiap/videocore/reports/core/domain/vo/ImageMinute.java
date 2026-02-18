package com.soat.fiap.videocore.reports.core.domain.vo;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;

/** Objeto de valor que representa o minuto em que uma imagem foi capturada. */
public record ImageMinute(long value) {
	public ImageMinute {
		validate(value);
	}

	private static void validate(long value) {
		if (value < 0) {
			throw new ReportException("O minuto em que a imagem foi capturada não deve ser inferior a 0");
		}
	}
}
