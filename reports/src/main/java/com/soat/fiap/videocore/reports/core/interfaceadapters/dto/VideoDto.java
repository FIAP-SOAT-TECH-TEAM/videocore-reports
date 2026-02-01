package com.soat.fiap.videocore.reports.core.interfaceadapters.dto;

import java.util.UUID;

/**
 * DTO para transporte de dados de vídeo entre camadas.
 *
 * @param videoName nome do vídeo
 * @param minuteFrameCut tempo de corte dos frames para captura de imaagens em minutos
 * @param userId identificador do usuário
 * @param requestId identificador da requisição
 */
public record VideoDto(
        String videoName,
        long minuteFrameCut,
        UUID userId,
        UUID requestId
) {
}