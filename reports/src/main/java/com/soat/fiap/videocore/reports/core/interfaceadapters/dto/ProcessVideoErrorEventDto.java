package com.soat.fiap.videocore.reports.core.interfaceadapters.dto;

import java.time.Instant;

/**
 * Evento de domínio que representa um erro no processamento do vídeo.
 *
 * @param videoName
 *            Nome do vídeo.
 * @param userId
 *            Identificador do usuário dono do vídeo.
 * @param requestId
 *            Identificador da requisição de processamento.
 * @param traceId
 *            Identificador de rastreio (observabilidade).
 * @param frameCutMinutes
 *            Intervalo de corte de frames em minutos.
 * @param percentStatusProcess
 *            Percentual do vídeo já processado.
 * @param reportTime
 *            Momento em que o erro foi detectado.
 */
public record ProcessVideoErrorEventDto(String videoName, String userId, String requestId, String traceId,
		long frameCutMinutes, Double percentStatusProcess, Instant reportTime) {
}
