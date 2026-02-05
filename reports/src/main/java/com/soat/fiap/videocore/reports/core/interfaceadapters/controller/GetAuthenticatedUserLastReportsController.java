package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthenticatedUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller responsável por orquestrar a busca da lista de reportes mais recentes dos videos enviados pelo usuário autenticado.
 */
@Component
@RequiredArgsConstructor
public class GetAuthenticatedUserLastReportsController {

    private final GetAuthenticatedUserLastReportsUseCase getAuthenticatedUserLastReportsUseCase;
    private final ReportPresenter reportPresenter;

    /**
     * Retorna a lista de reportes mais recentes dos videos enviados pelo usuário autenticado.
     *
     * @return lista de reportes convertidos para resposta HTTP
     */
    @WithSpan(name = "controller.get.all.authenticated.user.reports")
    public List<ReportResponse> getAuthenticatedUserLastReports() {
        var reports = getAuthenticatedUserLastReportsUseCase.getAuthenticatedUserLastReports();

        return reportPresenter.toResponse(reports);
    }
}