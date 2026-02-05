package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use case responsável por recuperar os reportes de um usuário.
 */
@Component
@RequiredArgsConstructor
public class GetUserLastReportsUseCase {

    private final ReportGateway reportGateway;

    /**
     * Recupera os reportes mais recentes dos videos enviados por um usuário.
     *
     * @param userId identificador do usuário
     * @return lista de {@link Report} (pode ser vazia)
     */
    @WithSpan(name = "usecase.get.all.user.reports")
    public List<Report> getUserLastReports(String userId){
        if (userId == null || userId.isBlank())
            throw new ReportException("O ID do usuário não pode estar em branco para pesquisa de reportes");

        return reportGateway.getLastReportsByUserId(userId);
    }
}