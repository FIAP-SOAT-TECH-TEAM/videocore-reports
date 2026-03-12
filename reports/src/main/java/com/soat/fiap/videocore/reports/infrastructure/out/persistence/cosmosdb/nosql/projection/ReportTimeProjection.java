package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection;

import java.time.Instant;

/**
 * Projeção utilizada para representar o momento do reporte em consultas ao
 * Cosmos DB, especialmente em queries com agregações.
 *
 * @param reportTime
 *            momento do reporte
 */
public record ReportTimeProjection(Instant reportTime) {
}
