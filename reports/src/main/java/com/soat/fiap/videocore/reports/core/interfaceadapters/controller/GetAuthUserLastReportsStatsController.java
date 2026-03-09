package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.output.ReportsStatsOutput;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsStatsUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportsStatsResponse;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por orquestrar a busca de estatísticas dos últimos
 * {@link com.soat.fiap.videocore.reports.core.domain.model.Report} enviados
 * pelo usuário autenticado.
 */
@Component @RequiredArgsConstructor
public class GetAuthUserLastReportsStatsController {

	private final GetAuthUserLastReportsStatsUseCase getAuthUserLastReportsStatsUseCase;
	private final ReportPresenter reportPresenter;

	/**
	 * Retorna estatísticas agregadas dos últimos reportes de processamento de
	 * vídeos enviados pelo usuário autenticado.
	 *
	 * @return estatísticas convertidas para {@link ReportsStatsResponse}
	 */
	@WithSpan(name = "controller.get.authenticated.user.reports.stats")
	public ReportsStatsResponse getAuthUserLastReportsStats() {
		ReportsStatsOutput stats = getAuthUserLastReportsStatsUseCase.getAuthUserLastReportsStats();

		return reportPresenter.toStatsResponse(stats);
	}
}
