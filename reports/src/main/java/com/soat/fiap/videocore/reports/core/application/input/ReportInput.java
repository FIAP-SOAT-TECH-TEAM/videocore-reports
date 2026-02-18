package com.soat.fiap.videocore.reports.core.application.input;

import java.time.Instant;

/**
 * Representa um DTO de entrada da aplicação (Application Layer), contendo
 * apenas os dados necessários para processar um reporte de atualização no
 * status de processamento de um vídeo.
 *
 * @param videoName
 *            Nome do vídeo.
 * @param userId
 *            Identificador do usuário dono do vídeo.
 * @param requestId
 *            Identificador da requisição de processamento.
 * @param traceId
 *            Identificador de rastreio (observabilidade).
 * @param imageMinute
 *            Minuto em que a imagem foi capturada.
 * @param frameCutMinutes
 *            Intervalo de corte de frames em minutos.
 * @param percentStatusProcess
 *            Percentual do vídeo já processado.
 * @param reportTime
 *            Momento em que o reporte foi realizado.
 * @param isError
 *            Indica se o reporte se trata de um erro.
 */
public record ReportInput(String videoName, String userId, String requestId, String traceId, long imageMinute,
		long frameCutMinutes, Double percentStatusProcess, Instant reportTime, boolean isError) {
}
