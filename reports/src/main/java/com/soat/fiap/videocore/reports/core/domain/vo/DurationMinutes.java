package com.soat.fiap.videocore.reports.core.domain.vo;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;

/**
 * Objeto de valor que representa o tempo de duração total de um vídeo em minutos.
 */
public record DurationMinutes(long value) {
    public DurationMinutes {
        validate(value);
    }

    private static void validate(long value) {
        if (value <= 0) {
            throw new ReportException("O tempo de duração total do vídeo em minutos deve ser maior que zero");
        }
    }
}
