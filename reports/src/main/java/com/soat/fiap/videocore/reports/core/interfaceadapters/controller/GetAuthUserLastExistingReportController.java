package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastExistingReportUseCase;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por orquestrar a busca do último {@link Report}
 * existente de um vídeo enviado pelo usuário autenticado.
 */
@Component @RequiredArgsConstructor
public class GetAuthUserLastExistingReportController {

	private final GetAuthUserLastExistingReportUseCase getAuthUserLastExistingReportUseCase;
	private final ReportPresenter reportPresenter;

	/**
	 * Retorna o último {@link Report} existente para um vídeo enviado pelo usuário
	 * autenticado.
	 *
	 * @param requestId
	 *            identificador da requisição
	 * @param videoName
	 *            nome do vídeo
	 * @return reporte convertido para {@link ReportResponse}
	 */
	@WithSpan(name = "controller.get.last.existing.report")
	public ReportResponse getAuthUserLastExistingReport(String requestId, String videoName) {
		Report report = getAuthUserLastExistingReportUseCase.getAuthUserLastExistingReport(requestId, videoName);

		return reportPresenter.toResponse(report);
	}
}
