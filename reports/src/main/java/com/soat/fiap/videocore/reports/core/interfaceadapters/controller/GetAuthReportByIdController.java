package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthReportByIdUseCase;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por orquestrar a busca de um {@link Report} do usuário autenticado pelo seu
 * identificador único.
 */
@Component @RequiredArgsConstructor
public class GetAuthReportByIdController {

	private final GetAuthReportByIdUseCase getAuthReportByIdUseCase;
	private final ReportPresenter reportPresenter;

	/**
	 * Retorna um {@link Report} convertido para resposta HTTP com base no
	 * {@code reportId} fornecido.
	 *
	 * @param reportId
	 *            identificador do reporte a ser buscado
	 * @return reporte convertido para {@link ReportResponse}
	 */
	@WithSpan(name = "controller.get.report.by.id")
	public ReportResponse getReportById(String reportId) {
		var report = getAuthReportByIdUseCase.getReportById(reportId);
		return reportPresenter.toResponse(report);
	}
}
