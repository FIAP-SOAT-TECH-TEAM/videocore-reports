package com.soat.fiap.videocore.reports.infrastructure.in.websocket.payload;

import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;

import java.time.Instant;

/**
 * Payload que representa um reporte de atualização do status de processamento de um vídeo
 * @param id                    Identificador único do reporte. Gerado por fontes externas
 * @param videoName             Nome do vídeo.
 * @param userId                Identificador do usuário dono do vídeo.
 * @param requestId             Identificador da requisição de processamento.
 * @param imageMinute           Minuto em que a imagem foi capturada
 * @param frameCutMinutes       Intervalo de corte de frames em minutos.
 * @param percentStatusProcess  Percentual do vídeo já processado.
 * @param reportTime            Momento em que o reporte foi realizado.
 * @param status                Status de processamento.
 */
public record ReportPayload(
        String id,
        String videoName,
        String userId,
        String requestId,
        long imageMinute,
        long frameCutMinutes,
        Double percentStatusProcess,
        Instant reportTime,
        ProcessStatus status
) {

}