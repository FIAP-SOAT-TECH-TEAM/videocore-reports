package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity.ReportEntity;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection.ProcessStatusProjection;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection.ReportTimeProjection;

/**
 * Repositório para operações de persistência de {@link ReportEntity} no Cosmos
 * DB.
 */
@Repository
public interface CosmosDbReportRepository extends CosmosRepository<ReportEntity, String> {
	/**
	 * Busca um {@link ReportEntity} por {@code userId}, {@code requestId},
	 * {@code videoName} e {@code
	 * percentStatusProcess}.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @param requestId
	 *            identificador da requisição
	 * @param videoName
	 *            nome do vídeo
	 * @param percentStatusProcess
	 *            percentual do status do processamento
	 * @return {@link Optional} com o reporte encontrado, ou vazio se não existir
	 */
	Optional<ReportEntity> findByUserIdAndRequestIdAndVideoNameAndPercentStatusProcess(String userId, String requestId,
			String videoName, Double percentStatusProcess);

	/**
	 * Busca o último {@link ReportEntity} persistido para as chaves {@code userId},
	 * {@code requestId} e {@code videoName}.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @param requestId
	 *            identificador da requisição
	 * @param videoName
	 *            nome do vídeo
	 * @return {@link Optional} com o último reporte encontrado, ou vazio se não
	 *         existir
	 */
	Optional<ReportEntity> findTopByUserIdAndRequestIdAndVideoNameOrderByReportTimeDesc(String userId, String requestId,
			String videoName);

	/**
	 * Retorna o momento de reporte dos últimos reportes de cada requestId e
	 * videoName de um usuário.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @return momentos de reporte mais recentes
	 */
	@Query("SELECT MAX(r.reportTime) AS reportTime FROM report r WHERE r.userId = @userId GROUP BY r.requestId, r.videoName")
	List<ReportTimeProjection> findLatestReportsTimesByUser(String userId);

	/**
	 * Busca os reportes correspondentes aos momentos de reporte fornecidos. Suporta
	 * paginação
	 *
	 * @param reportTimes
	 *            lista de momentos de reporte
	 * @param pageable
	 *            configuração de paginação
	 * @return lista de entidades de report
	 */
	@Query("SELECT * FROM r WHERE r.reportTime IN (@reportTimes)")
	Page<ReportEntity> findByReportTimeIn(List<String> reportTimes, Pageable pageable);

	/**
	 * Busca os reportes correspondentes aos momentos de reporte fornecidos.
	 *
	 * @param reportTimes
	 *            lista de momentos de reporte
	 * @param sort
	 *            objeto de ordenação
	 * @return lista de entidades de report
	 */
	List<ReportEntity> findByReportTimeIn(List<String> reportTimes, Sort sort);

	/**
	 * Busca os reportes correspondentes aos momentos de reporte fornecidos.
	 *
	 * @param reportTimes
	 *            lista de momentos de reporte
	 * @return lista de entidades de report
	 */
	List<ReportEntity> findByReportTimeIn(List<String> reportTimes);

	/**
	 * Busca os status de reportes correspondentes aos momentos de reporte
	 * fornecidos.
	 *
	 * @param reportTimes
	 *            lista de momentos de reporte
	 * @return lista de status de reporte
	 */
	@Query("SELECT r.status AS processStatus FROM report r WHERE r.reportTime IN (@reportTimes)")
	List<ProcessStatusProjection> findStatusByReportTimeIn(List<String> reportTimes);

	/**
	 * Conta a quantidade de registros agregados por {@code requestId} e
	 * {@code videoName} para um usuário. Utilizado para obter o total de itens após
	 * a agregação, contornando limitações de paginação com {@code GROUP BY} no
	 * Cosmos DB.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @return total de registros agregados
	 */
	@Query("SELECT VALUE COUNT(1) FROM (SELECT r.videoName FROM report r WHERE r.userId = @userId GROUP BY r.requestId, r.videoName)")
	long countLatestReportsByUser(String userId);
}
