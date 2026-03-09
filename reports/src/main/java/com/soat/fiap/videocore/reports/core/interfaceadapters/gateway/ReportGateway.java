package com.soat.fiap.videocore.reports.core.interfaceadapters.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.TraceContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.core.interfaceadapters.mapper.ReportMapper;
import com.soat.fiap.videocore.reports.infrastructure.common.source.ReportDataSource;

import lombok.RequiredArgsConstructor;

/**
 * Gateway responsável por persistir objetos {@link Report} usando a camada de
 * persistência subjacente.
 */
@Component @RequiredArgsConstructor
public class ReportGateway {
	private final ReportDataSource reportDataSource;
	private final ReportMapper reportMapper;

	/**
	 * Persiste um {@link Report} no datasource e retorna a versão salva.
	 *
	 * @param report
	 *            objeto {@link Report} a ser salvo
	 * @return o {@link Report} salvo, mapeado do DTO retornado pelo datasource
	 */
	@WithSpan(name = "gateway.save.report")
	public Report save(Report report) {
		var dto = reportMapper.toDto(report);
		var savedDto = reportDataSource.save(dto);

		return reportMapper.toModel(savedDto);
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
	 * @return {@link Optional} com o {@link Report} encontrado, ou vazio se não
	 *         existir
	 */
	@WithSpan(name = "gateway.get.report.existing")
	public Optional<Report> getExistingReport(String userId, String requestId, String videoName,
			Double percentStatusProcess) {
		var dto = reportDataSource.getExistingReport(userId, requestId, videoName, percentStatusProcess);

		TraceContext.addEvent("report.object", dto);

		return dto.map(reportMapper::toModel);
	}

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
	@WithSpan(name = "gateway.get.last.report.existing")
	public Optional<Report> getLastExistingReport(String userId, String requestId, String videoName) {

		var dto = reportDataSource.getLastExistingReport(userId, requestId, videoName);

		TraceContext.addEvent("report.object", dto);

		return dto.map(reportMapper::toModel);
	}

	/**
	 * Recupera os reportes mais recentes dos videos enviados por um usuário.
	 * Suporta paginação
	 *
	 * @param userId
	 *            identificador do usuário
	 * @param page
	 *            número da página
	 * @param size
	 *            quantidade de elementos por página
	 * @return lista de {@link Report} encontrados (pode ser vazia)
	 */
	@WithSpan(name = "gateway.get.last.reports.by.userId")
	public PaginationDTO<Report> getLastReportsByUserId(String userId, int page, int size) {

		var dtos = reportDataSource.getLastReportsByUserId(userId, page, size);

		TraceContext.addEvent("report.object.list", dtos);

		return reportMapper.toPaginationModel(dtos);
	}

	/**
	 * Recupera os reportes mais recentes dos videos enviados por um usuário.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @return lista de {@link Report} encontrados (pode ser vazia)
	 */
	@WithSpan(name = "gateway.get.last.reports.by.userId")
	public List<Report> getLastReportsByUserId(String userId) {

		var dtos = reportDataSource.getLastReportsByUserId(userId);

		TraceContext.addEvent("report.object.list", dtos);

		return dtos.stream().map(reportMapper::toModel).toList();
	}

	/**
	 * Recupera os status de reporte mais recentes dos videos enviados por um
	 * usuário.
	 *
	 * @param userId
	 *            identificador do usuário
	 * @return lista de {@link ProcessStatus} encontrados (pode ser vazia)
	 */
	@WithSpan(name = "gateway.get.last.reports.status.by.userId")
	public List<ProcessStatus> getLastReportsStatusByUserId(String userId) {
		var status = reportDataSource.getLastReportsStatusByUserId(userId);

		TraceContext.addEvent("report.status.object.list", status);

		return status;
	}
}
