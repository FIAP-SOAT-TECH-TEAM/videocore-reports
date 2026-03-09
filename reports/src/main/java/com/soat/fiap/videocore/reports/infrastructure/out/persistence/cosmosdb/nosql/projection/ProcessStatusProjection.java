package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection;

/**
 * Projeção utilizada para representar o status de processamento de um vídeo em
 * consultas ao Cosmos DB.
 *
 * @param processStatus
 *            status de processamento
 */
public record ProcessStatusProjection(String processStatus) {
}
