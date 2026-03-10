package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.common.exceptions.persistence.OrderParamException;
import com.soat.fiap.videocore.reports.infrastructure.common.source.ReportDataSource;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper.ReportEntityMapper;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection.ProcessStatusProjection;
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
	 * @param orderField
	 *            campo de ordenação
	 * @param orderDirection
	 *            direção da ordenação
	 * @return objeto contendo metadados de paginação e os reportes encontrados
	 */
	@Override @Transactional(readOnly = true)
	public PaginationDTO<ReportDto> getLastReportsByUserId(String userId, int page, int size, String orderField,
			String orderDirection) {
		Sort sort = null;
		try {
			if (orderField != null && !orderField.isEmpty() && orderDirection != null && !orderDirection.isEmpty())
				sort = Sort.by(Sort.Direction.fromString(orderDirection), orderField);
		} catch (IllegalArgumentException ex) {
			throw new OrderParamException(
					"Parâmetros de ordenação inválidos. Revise o atributo escolhido e a direção (asc ou desc)");
		}

		var pageRequest = sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);

		var reportsTime = cosmosDbReportRepository.findLatestReportsTimesByUser(userId);

		if (reportsTime.isEmpty())
			return new PaginationDTO<>(page, size, 0, 0, false, false, List.of());

		var reportTimes = reportsTime.stream().map(ReportTimeProjection::reportTime).map(Instant::toString).toList();

		var reportsPage = cosmosDbReportRepository.findByReportTimeIn(reportTimes, pageRequest);
		var reports = reportsPage.stream().map(reportEntityMapper::toDto).toList();

		return reportEntityMapper.toPaginationDTO(reportsPage, reports);
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
	 * @param orderField
	 *            campo de ordenação
	 * @param orderDirection
	 *            direção da ordenação
	 * @return objeto contendo os reportes encontrados
	 */
	@Override @Transactional(readOnly = true)
	public List<ReportDto> getLastReportsByUserId(String userId, String orderField, String orderDirection) {

		Sort sort = null;
		try {
			if (orderField != null && !orderField.isEmpty() && orderDirection != null && !orderDirection.isEmpty())
				sort = Sort.by(Sort.Direction.fromString(orderDirection), orderField);
		} catch (IllegalArgumentException ex) {
			throw new OrderParamException(
					"Parâmetros de ordenação inválidos. Revise o atributo escolhido e a direção (asc ou desc)");
		}

		var reportTimes = cosmosDbReportRepository.findLatestReportsTimesByUser(userId)
				.stream()
				.map(ReportTimeProjection::reportTime)
				.toList();

		if (reportTimes.isEmpty())
			return List.of();

		var reportTimesAsText = reportTimes.stream().map(Instant::toString).toList();

		return sort != null
				? cosmosDbReportRepository.findByReportTimeIn(reportTimesAsText, sort)
						.stream()
						.map(reportEntityMapper::toDto)
						.toList()
				: cosmosDbReportRepository.findByReportTimeIn(reportTimesAsText)
						.stream()
						.map(reportEntityMapper::toDto)
						.toList();
	}

	/**
	 * Recupera os status de reporte mais recentes dos videos enviados por um
	 * usuário.
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
	 * @return objeto contendo os status de reporte encontrados
	 */
	@Override @Transactional(readOnly = true)
	public List<ProcessStatus> getLastReportsStatusByUserId(String userId) {
		var reportTimes = cosmosDbReportRepository.findLatestReportsTimesByUser(userId)
				.stream()
				.map(ReportTimeProjection::reportTime)
				.toList();

		if (reportTimes.isEmpty())
			return List.of();

		var reportTimesAsText = reportTimes.stream().map(Instant::toString).toList();
		var processStatusAsText = cosmosDbReportRepository.findStatusByReportTimeIn(reportTimesAsText)
				.stream()
				.map(ProcessStatusProjection::processStatus)
				.toList();

		return processStatusAsText.stream().map(ProcessStatus::valueOf).toList();
	}
}
