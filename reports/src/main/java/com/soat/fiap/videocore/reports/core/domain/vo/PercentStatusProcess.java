package com.soat.fiap.videocore.reports.core.domain.vo;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;

/**
 * Objeto de valor que representa o percentual de progresso do processamento do vídeo.
 *
 * @param value Percentual de progresso, entre 0 e 100.
 */
public record PercentStatusProcess(Double value) {

    public PercentStatusProcess {
        if (value < 0.0 || value > 100.0) {
            throw new ReportException(
                    "O percentual de processamento do vídeo deve estar entre 0 e 100, valor fornecido: " + value
            );
        }
    }
}