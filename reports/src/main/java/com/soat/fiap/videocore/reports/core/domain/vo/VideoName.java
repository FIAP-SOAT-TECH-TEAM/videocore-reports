package com.soat.fiap.videocore.reports.core.domain.vo;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;

/**
 * Objeto de valor que representa o nome de um vídeo.
 */
public record VideoName(String value) {

    public VideoName {
        validate(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new ReportException("O nome do vídeo não pode ser nulo ou vazio");
        }
    }
}