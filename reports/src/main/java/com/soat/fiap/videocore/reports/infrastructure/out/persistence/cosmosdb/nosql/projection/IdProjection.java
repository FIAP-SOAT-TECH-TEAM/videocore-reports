package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection;

/**
 * Projeção utilizada para representar o identificador de um registro retornado
 * em consultas ao Cosmos DB, especialmente em queries com agregações.
 *
 * @param id identificador único do registro
 */
public record IdProjection(String id) {
}