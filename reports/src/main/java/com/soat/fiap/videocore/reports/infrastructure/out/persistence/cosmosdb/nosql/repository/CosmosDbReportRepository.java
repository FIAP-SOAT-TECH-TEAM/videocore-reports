package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity.ReportEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações de persistência de {@link ReportEntity} no Cosmos DB.
 */
@Repository
public interface CosmosDbReportRepository extends CosmosRepository<ReportEntity, String> {
    /**
     * Busca um {@link ReportEntity} por {@code userId}, {@code requestId}, {@code videoName}
     * e {@code percentStatusProcess}.
     *
     * @param userId               identificador do usuário (partition key)
     * @param requestId            identificador da requisição
     * @param videoName            nome do vídeo
     * @param percentStatusProcess percentual do status do processamento
     * @return {@link Optional} com o reporte encontrado, ou vazio se não existir
     */
    Optional<ReportEntity> findByUserIdAndRequestIdAndVideoNameAndPercentStatusProcess(
            String userId,
            String requestId,
            String videoName,
            Double percentStatusProcess
    );

}