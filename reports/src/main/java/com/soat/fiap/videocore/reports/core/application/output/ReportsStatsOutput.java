package com.soat.fiap.videocore.reports.core.application.output;

/**
 * Representa um DTO de saída da aplicação (Application Layer), contendo
 * estatísticas agregadas dos reportes de um usuário.
 *
 * @param total
 *            Quantidade total de reportes.
 * @param completed
 *            Quantidade de reportes com status COMPLETED.
 * @param processing
 *            Quantidade de reportes em processamento, incluindo os status
 *            PROCESSING e STARTED.
 * @param failed
 *            Quantidade de reportes com status FAILED.
 */
public record ReportsStatsOutput(long total, long completed, long processing, long failed) {
}
