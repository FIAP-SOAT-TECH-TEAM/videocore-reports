package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por orquestrar a busca da lista de reportes mais
 * recentes dos videos enviados pelo usuário autenticado.
 */
@Component @RequiredArgsConstructor
public class GetAuthenticatedUserLastReportsController {

	private final GetAuthUserLastReportsUseCase getAuthUserLastReportsUseCase;
	private final ReportPresenter reportPresenter;

	/**
	 * Retorna a lista de reportes mais recentes dos videos enviados pelo usuário
	 * autenticado.
	 *
	 * @return lista de reportes convertidos para resposta HTTP
	 */
	@WithSpan(name = "controller.get.authenticated.user.all.reports")
	public List<ReportResponse> getAuthenticatedUserLastReports() {
		var reports = getAuthUserLastReportsUseCase.getAuthenticatedUserLastReports();

		return reportPresenter.toResponse(reports);
	}
}
