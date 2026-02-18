package com.soat.fiap.videocore.reports.core.interfaceadapters.presenter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

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
}
