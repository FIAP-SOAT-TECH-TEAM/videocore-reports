package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.PaginationResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por orquestrar a busca da lista de reportes mais
 * recentes dos videos enviados pelo usuário autenticado.
 */
@Component @RequiredArgsConstructor
public class GetAuthUserLastReportsController {

	private final GetAuthUserLastReportsUseCase getAuthUserLastReportsUseCase;
	private final ReportPresenter reportPresenter;

	/**
	 * Retorna a lista de reportes mais recentes dos videos enviados pelo usuário
	 * autenticado. Suporta paginação
	 *
	 * @param page
	 *            número da página
	 * @param size
	 *            quantidade de elementos por página
	 * @param orderField
	 *            campo de ordenação
	 * @param orderDirection
	 *            direção da ordenação
	 *
	 * @return lista de reportes convertidos para resposta HTTP
	 */
	@WithSpan(name = "controller.get.authenticated.user.all.reports")
	public PaginationResponse<ReportResponse> getAuthenticatedUserLastReports(int page, int size, String orderField,
			String orderDirection) {
		var reports = getAuthUserLastReportsUseCase.getAuthenticatedUserLastReports(page, size, orderField,
				orderDirection);

		return reportPresenter.toPaginationResponse(reports);
	}

	/**
	 * Retorna a lista de reportes mais recentes dos videos enviados pelo usuário
	 * autenticado.
	 *
	 * @param orderField
	 *            campo de ordenação
	 * @param orderDirection
	 *            direção da ordenação
	 *
	 * @return lista de reportes convertidos para resposta HTTP
	 */
	@WithSpan(name = "controller.get.authenticated.user.all.reports")
	public List<ReportResponse> getAuthenticatedUserLastReports(String orderField, String orderDirection) {
		var reports = getAuthUserLastReportsUseCase.getAuthenticatedUserLastReports(orderField, orderDirection);

		return reportPresenter.toResponse(reports);
	}
}
