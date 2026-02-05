package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.core.application.usecase.GetUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller responsável por orquestrar a busca de todos os reportes de um usuário.
 */
@Component
@RequiredArgsConstructor
public class GetUserLastReportsController {

    private final GetUserLastReportsUseCase getUserLastReportsUseCase;
    private final ReportPresenter reportPresenter;

    /**
     * Retorna a lista de reportes associados ao usuário informado.
     *
     * @param userId identificador do usuário
     * @return lista de reportes convertidos para resposta HTTP
     */
    public List<ReportResponse> getAllUserReports(String userId) {
        var reports = getUserLastReportsUseCase.getUserLastReports(userId);

        return reportPresenter.toResponse(reports);
    }
}