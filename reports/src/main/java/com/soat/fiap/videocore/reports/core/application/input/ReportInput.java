package com.soat.fiap.videocore.reports.core.application.input;

import java.time.Instant;
import java.util.UUID;

/**
 * Representa um DTO de entrada da aplicação (Application Layer), contendo
 * apenas os dados necessários para processar um reporte de atualização no status de processamento de um vídeo.
 * @param videoName             Nome do vídeo.
 * @param userId                Identificador do usuário dono do vídeo.
 * @param requestId             Identificador da requisição de processamento.
 * @param durationMinutes       Duração total do vídeo em minutos.
 * @param frameCutMinutes       Intervalo de corte de frames em minutos.
 * @param percentStatusProcess  Percentual do vídeo já processado.
 * @param reportTime            Momento em que o reporte foi realizado.
 */
public record ReportInput(
        String videoName,
        UUID userId,
        UUID requestId,
        long durationMinutes,
        long frameCutMinutes,
        Double percentStatusProcess,
        Instant reportTime
) {

}