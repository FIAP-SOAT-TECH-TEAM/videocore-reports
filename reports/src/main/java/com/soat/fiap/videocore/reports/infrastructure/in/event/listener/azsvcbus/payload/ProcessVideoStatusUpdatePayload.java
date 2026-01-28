package com.soat.fiap.videocore.reports.infrastructure.in.event.listener.azsvcbus.payload;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidade de infraestrutura que representa a atualização do status de processamento de um vídeo.
 *
 * @param videoName             Nome do vídeo.
 * @param userId                Identificador do usuário dono do vídeo.
 * @param requestId             Identificador da requisição de processamento.
 * @param durationMinutes       Duração total do vídeo em minutos.
 * @param frameCutMinutes       Intervalo de corte de frames em minutos.
 * @param percentStatusProcess  Percentual do vídeo já processado.
 * @param reportTime            Momento em que o reporte foi realizado.
 */
public record ProcessVideoStatusUpdatePayload(
        String videoName,
        UUID userId,
        UUID requestId,
        long durationMinutes,
        long frameCutMinutes,
        Double percentStatusProcess,
        Instant reportTime
) {

}