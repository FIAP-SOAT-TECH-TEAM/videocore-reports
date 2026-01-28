package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity.ReportEntity;
import org.springframework.stereotype.Repository;

/**
 * Repositório para operações de persistência de {@link ReportEntity} no Cosmos DB.
 */
@Repository
public interface CosmosDbReportRepository extends CosmosRepository<ReportEntity, String> {
}