package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity.ReportEntity;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection.ReportTimeProjection;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência de {@link ReportEntity} no Cosmos DB.
 */
@Repository
@Primary
public interface CosmosDbReportRepository extends CosmosRepository<ReportEntity, String> {
    /**
     * Busca um {@link ReportEntity} por {@code userId}, {@code requestId}, {@code videoName}
     * e {@code percentStatusProcess}.
     *
     * @param userId               identificador do usuário
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

    /**
     * Busca o último {@link ReportEntity} persistido para as chaves {@code userId}, {@code requestId}
     * e {@code videoName}.
     *
     * @param userId    identificador do usuário
     * @param requestId identificador da requisição
     * @param videoName nome do vídeo
     * @return {@link Optional} com o último reporte encontrado, ou vazio se não existir
     */
    Optional<ReportEntity> findTopByUserIdAndRequestIdAndVideoNameOrderByReportTimeDesc(
            String userId,
            String requestId,
            String videoName
    );


    /**
     * Retorna o momento de reporte dos últimos reports de cada requestId e videoName de um usuário.
     *
     * @param userId identificador do usuário
     * @return lista de momentos de reporte mais recentes
     */
    @Query("SELECT MAX(r.reportTime) AS id FROM report r WHERE r.userId = @userId GROUP BY r.requestId, r.videoName")
    List<ReportTimeProjection> findLatestReportsTimesByUser(String userId);

    /**
     * Busca os reportes correspondentes aos momentos de reporte fornecidos.
     *
     * @param reportTimes lista de momentos de reporte
     * @return lista de entidades de report
     */
    List<ReportEntity> findByReportTimeIn(List<String> reportTimes);


}