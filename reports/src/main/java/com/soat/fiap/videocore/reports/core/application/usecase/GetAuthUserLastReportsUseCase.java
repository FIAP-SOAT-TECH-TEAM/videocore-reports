package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Use case responsável por os reportes mais recentes dos videos enviados pelo usuário autenticado.
 */
@Component
@RequiredArgsConstructor
public class GetAuthUserLastReportsUseCase {

    private final ReportGateway reportGateway;
    private final AuthenticatedUserGateway authenticatedUserGateway;

    /**
     * Recupera os reportes mais recentes dos videos enviados pelo usuário autenticado.
     *
     * @return lista de {@link Report} (pode ser vazia)
     */
    @WithSpan(name = "usecase.get.authenticated.user.all.reports")
    public List<Report> getAuthenticatedUserLastReports(){
        var userId = authenticatedUserGateway.getSubject();

        CanonicalContext.add("user_id", userId);

        if (userId == null || userId.isBlank())
            throw new NotAuthorizedException("O ID do usuário não pode estar em branco para pesquisa de reportes. Verifique a autenticação.");

        return reportGateway.getLastReportsByUserId(userId);
    }
}