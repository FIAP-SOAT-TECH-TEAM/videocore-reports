package com.soat.fiap.videocore.reports.infrastructure.in.http.response;

import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Entidade de resposta para solicitações de reporte via API HTTP
 */
@Schema(name = "ReportResponse", description = "Representa os dados de um reporte de atualização do status de processamento de um vídeo")
public record ReportResponse(
        @Schema(description = "Identificador único do reporte. Gerado por fontes externas", example = "abc123")
        String id,

        @Schema(description = "Nome do vídeo", example = "video_exemplo.mp4")
        String videoName,

        @Schema(description = "Identificador do usuário dono do vídeo", example = "3f1e2c4a-8b7d-4e5f-9a1b-2c3d4e5f6a7b")
        String userId,

        @Schema(description = "Identificador da requisição de processamento", example = "9a8b7c6d-5e4f-3a2b-1c0d-9e8f7a6b5c4d")
        String requestId,

        @Schema(description = "Intervalo de corte de frames em minutos", example = "5")
        long frameCutMinutes,

        @Schema(description = "Percentual do vídeo já processado", example = "50")
        Double percentStatusProcess,

        @Schema(description = "Momento em que o reporte foi realizado", example = "2026-02-04T14:00:00Z")
        Instant reportTime,

        @Schema(description = "Status de processamento do vídeo", example = "PROCESSING")
        ProcessStatus status
) {

}