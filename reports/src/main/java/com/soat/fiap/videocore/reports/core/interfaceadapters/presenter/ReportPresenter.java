package com.soat.fiap.videocore.reports.core.interfaceadapters.presenter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.soat.fiap.videocore.reports.core.application.output.ReportsStatsOutput;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.PaginationResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportsStatsResponse;

/**
 * Presenter responsável por converter objetos de domínio ({@link Report}) em
 * objetos de resposta para a camada de entrada HTTP ({@link ReportResponse}).
 */
@Mapper(componentModel = "spring")
public interface ReportPresenter {

	/**
	 * Converte um {@link Report} (domínio) para {@link ReportResponse} (DTO de
	 * saída HTTP).
	 *
	 * @param report
	 *            objeto de domínio a ser convertido
	 * @return DTO de resposta HTTP equivalente ao reporte informado
	 */
	@Mapping(target = "videoName", expression = "java(report.getVideoName())")
	@Mapping(target = "userId", expression = "java(report.getUserId())")
	@Mapping(target = "requestId", expression = "java(report.getRequestId())")
	@Mapping(target = "traceId", expression = "java(report.getTraceId())")
	@Mapping(target = "frameCutMinutes", expression = "java(report.getMinuteFrameCut())")
	@Mapping(target = "percentStatusProcess", expression = "java(report.getPercentStatusProcess())")
	ReportResponse toResponse(Report report);

	List<ReportResponse> toResponse(List<Report> reports);

	/**
	 * Converte um {@link PaginationDTO} de {@link Report} para
	 * {@link PaginationResponse} de {@link ReportResponse}.
	 *
	 * @param pagination
	 *            página contendo modelos de domínio
	 * @return resposta paginada contendo DTOs de saída HTTP
	 */
	PaginationResponse<ReportResponse> toPaginationResponse(PaginationDTO<Report> pagination);

	/**
	 * Converte um {@link ReportsStatsOutput} (modelo de saída da aplicação) para
	 * {@link ReportsStatsResponse} (DTO de resposta HTTP).
	 *
	 * @param stats
	 *            objeto contendo estatísticas agregadas dos reportes
	 * @return DTO de resposta HTTP contendo as estatísticas
	 */
	ReportsStatsResponse toStatsResponse(ReportsStatsOutput stats);
}
