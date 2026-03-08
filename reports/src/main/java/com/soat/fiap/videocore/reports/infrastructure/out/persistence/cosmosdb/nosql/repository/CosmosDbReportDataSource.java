package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.azure.spring.data.cosmos.core.query.CosmosPageRequest;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.common.source.ReportDataSource;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper.ReportEntityMapper;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection.ReportTimeProjection;

import lombok.RequiredArgsConstructor;

/** Implementação de {@link ReportDataSource} usando Cosmos DB. */
@Component @RequiredArgsConstructor
public class CosmosDbReportDataSource implements ReportDataSource {
	private final CosmosDbReportRepository cosmosDbReportRepository;
	private final ReportEntityMapper reportEntityMapper;

	/**
	 * Persiste o {@link ReportDto} no Cosmos DB e retorna a versão salva.
	 *
	 * @param reportDto
	 *            reporte a ser salvo
	 * @return reporte persistido
	 */
	@Override @Transactional
	public ReportDto save(ReportDto reportDto) {
		var entity = reportEntityMapper.toEntity(reportDto);
		var savedEntity = cosmosDbReportRepository.save(entity);

		return reportEntityMapper.toDto(savedEntity);
	}

	/**
	 * Recupera um reporte já existente a partir de {@code userId},
	 * {@code requestId}, {@code
	 * videoName} e {@code percentStatusProcess}.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @param requestId
	 *            identificador da requisição
	 * @param videoName
	 *            nome do vídeo
	 * @param percentStatusProcess
	 *            percentual do status do processamento
	 * @return {@link Optional} com o {@link ReportDto} encontrado, ou vazio se não
	 *         existir
	 */
	@Override @Transactional(readOnly = true)
	public Optional<ReportDto> getExistingReport(String userId, String requestId, String videoName,
			Double percentStatusProcess) {

		return cosmosDbReportRepository
				.findByUserIdAndRequestIdAndVideoNameAndPercentStatusProcess(userId, requestId, videoName,
						percentStatusProcess)
				.map(reportEntityMapper::toDto);
	}

	/**
	 * Recupera o último reporte persistido para {@code userId}, {@code requestId} e
	 * {@code
	 * videoName}, utilizando o atributo nativo {@code _ts} do Cosmos DB para
	 * determinar o registro mais recente.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @param requestId
	 *            identificador da requisição
	 * @param videoName
	 *            nome do vídeo
	 * @return {@link Optional} com o {@link ReportDto} encontrado, ou vazio se não
	 *         existir
	 */
	@Override @Transactional(readOnly = true)
	public Optional<ReportDto> getLastExistingReport(String userId, String requestId, String videoName) {

		return cosmosDbReportRepository
				.findTopByUserIdAndRequestIdAndVideoNameOrderByReportTimeDesc(userId, requestId, videoName)
				.map(reportEntityMapper::toDto);
	}

	/**
	 * Recupera os reportes mais recentes dos videos enviados por um usuário.
	 *
	 * <p>
	 * Cosmos DB não suporta non-correlated queries. Por isso, primeiro buscamos os
	 * momentos de reporte mais recentes, e depois os reportes efetivamente.
	 * <a href=
	 * "https://learn.microsoft.com/en-us/cosmos-db/query/subquery#types-of-subqueries">How
	 * to use subquery (SQL) in azure cosmos db</a> <a href=
	 * "https://learn.microsoft.com/en-us/answers/questions/528514/how-to-use-subquery-(sql)-in-azure-cosmos-db">Types
	 * of subqueries</a>
	 *
	 * <p>
	 * Além disso, consultas que utilizam {@code GROUP BY} possuem limitações de
	 * paginação no Cosmos DB, pois esse tipo de consulta não suporta continuation
	 * tokens, mecanismo utilizado pelo banco para percorrer resultados paginados.
	 * <a href="https://learn.microsoft.com/en-us/cosmos-db/query/pagination">
	 * Pagination in Azure Cosmos DB</a>
	 *
	 * @param userId
	 *            identificador do usuário
	 * @param page
	 *            número da página
	 * @param size
	 *            quantidade de elementos por página
	 * @return objeto contendo metadados de paginação e os reportes encontrados
	 */
	@Override @Transactional(readOnly = true)
	public PaginationDTO<ReportDto> getLastReportsByUserId(String userId, int page, int size) {
		var cosmosPageRequest = new CosmosPageRequest(page, size, null);

		var reportsSlice = cosmosDbReportRepository.findLatestReportsTimesByUser(userId, cosmosPageRequest);

		if (reportsSlice.isEmpty())
			return new PaginationDTO<>(page, size, 0, 0, false, false, List.of());

		var totalElements = cosmosDbReportRepository.countLatestReportsByUser(userId);
		var hasPrevious = page > 0;
		var hasNext = ((long) (page + 1) * size) < totalElements;
		var totalPages = (int) Math.ceil((double) totalElements / size);

		var reportTimes = reportsSlice.getContent()
				.stream()
				.map(ReportTimeProjection::id)
				.map(Instant::toString)
				.toList();

		var reports = cosmosDbReportRepository.findByReportTimeIn(reportTimes)
				.stream()
				.map(reportEntityMapper::toDto)
				.toList();

		return reportEntityMapper.toPaginationDTO(reportsSlice, reports, totalElements, totalPages, hasPrevious,
				hasNext);
	}
}
