package com.soat.fiap.videocore.reports.infrastructure.in.http.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidade de resposta contendo estatísticas agregadas dos reportes de
 * processamento de vídeos de um usuário.
 */
@Schema(name = "ReportsStatsResponse", description = "Representa estatísticas agregadas dos últimos reportes de processamento de vídeos de um usuário")
public record ReportsStatsResponse(

		@Schema(description = "Quantidade total de vídeos com reportes registrados", example = "10") long total,

		@Schema(description = "Quantidade de vídeos com processamento concluído", example = "6") long completed,

		@Schema(description = "Quantidade de vídeos em processamento", example = "3") long processing,

		@Schema(description = "Quantidade de vídeos com falha no processamento", example = "1") long failed) {
}
