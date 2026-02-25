package com.soat.fiap.videocore.reports.infrastructure.common.source;

import java.util.List;
import java.util.Optional;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;

/** Fonte de dados para operações de persistência de reportes. */
public interface ReportDataSource {

	/**
	 * Persiste o reporte fornecido e retorna a versão salva.
	 *
	 * @param reportDto
	 *            reporte a ser salvo
	 * @return reporte persistido
	 */
	ReportDto save(ReportDto reportDto);

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
	Optional<ReportDto> getExistingReport(String userId, String requestId, String videoName,
			Double percentStatusProcess);

	/**
	 * Recupera o último reporte persistido para {@code userId}, {@code requestId} e
	 * {@code
	 * videoName},
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
	Optional<ReportDto> getLastExistingReport(String userId, String requestId, String videoName);

	/**
	 * Recupera os reportes mais recentes dos videos enviados por um usuário.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @return lista com os últimos reportes encontrados (pode ser vazia)
	 */
	List<ReportDto> getLastReportsByUserId(String userId);

	/**
	 * Recupera um reporte pelo seu identificador único.
	 *
	 * @param reportId
	 *            identificador do reporte
	 * @return {@link Optional} com o {@link ReportDto} encontrado, ou vazio se não
	 *         existir
	 */
	Optional<ReportDto> getById(String reportId);
}
