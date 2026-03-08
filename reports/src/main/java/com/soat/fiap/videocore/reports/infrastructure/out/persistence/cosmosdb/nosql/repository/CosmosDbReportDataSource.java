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
	 * Suporta paginação.
	 *
	 * <p>
	 * <b>Limitações Técnicas do Cosmos DB & Spring Data:</b>
	 * <ul>
	 * <li><b>Subqueries:</b> O Cosmos DB não suporta <i>non-correlated
	 * subqueries</i> (ex: {@code WHERE IN (SELECT...)}). A operação deve ser
	 * dividida em duas etapas: extração manual de chaves e busca posterior dos
	 * documentos. <a href=
	 * "https://learn.microsoft.com/en-us/answers/questions/528514/how-to-use-subquery-(sql)-in-azure-cosmos-db">Subqueries
	 * in Azure Cosmos DB</a> e <a href=
	 * "https://learn.microsoft.com/en-us/cosmos-db/query/subquery#types-of-subqueries">Types
	 * of subqueries</a>.</li>
	 * <li><b>Agregação e Paginação:</b> Consultas que utilizam {@code GROUP BY} ou
	 * {@code DISTINCT} não suportam <i>continuation tokens</i>. Nesses casos, a
	 * paginação é feita via {@code OFFSET LIMIT}, que possui custo de RU crescente.
	 * <a href=
	 * "https://learn.microsoft.com/en-us/cosmos-db/query/pagination#continuation-tokens">Pagination
	 * in Azure Cosmos DB</a>.</li>
	 * <li><b>Spring Data Page:</b> O Spring Data Azure Cosmos DB não suporta o
	 * retorno de {@code Page<T>} em métodos anotados com {@code @Query}, sendo
	 * obrigatório o uso de {@code Slice<T>}. O total de elementos deve ser
	 * consultado separadamente para compor metadados. <a href=
	 * "https://github.com/microsoft/spring-data-cosmosdb/issues/559">Pagination and
	 * Sorting in Spring Data</a>.</li>
	 * </ul>
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
	 * @param userId
	 *            identificador do usuário
	 * @return objeto contendo metadados de paginação e os reportes encontrados
	 */
	@Override @Transactional(readOnly = true)
	public List<ReportDto> getLastReportsByUserId(String userId) {
		var reportTimes = cosmosDbReportRepository.findLatestReportsTimesByUser(userId)
				.stream()
				.map(ReportTimeProjection::id)
				.toList();

		if (reportTimes.isEmpty())
			return List.of();

		var reportTimesAsText = reportTimes.stream().map(Instant::toString).toList();

		return cosmosDbReportRepository.findByReportTimeIn(reportTimesAsText)
				.stream()
				.map(reportEntityMapper::toDto)
				.toList();
	}
}
