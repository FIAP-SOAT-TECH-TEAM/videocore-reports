package com.soat.fiap.videocore.reports.core.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

import lombok.RequiredArgsConstructor;

/**
 * Use case responsável por os reportes mais recentes dos videos enviados pelo
 * usuário autenticado.
 */
@Component @RequiredArgsConstructor
public class GetAuthUserLastReportsUseCase {

	private final ReportGateway reportGateway;
	private final AuthenticatedUserGateway authenticatedUserGateway;

	/**
	 * Recupera os reportes mais recentes dos videos enviados pelo usuário
	 * autenticado. Suporta paginação
	 *
	 * @param page
	 *            número da página
	 * @param size
	 *            quantidade de elementos por página
	 *
	 * @return lista de {@link Report} (pode ser vazia)
	 */
	@WithSpan(name = "usecase.get.authenticated.user.all.reports")
	public PaginationDTO<Report> getAuthenticatedUserLastReports(int page, int size) {
		CanonicalContext.add("page", page);
		CanonicalContext.add("size", size);

		if (page < 0)
			throw new ReportException("O número da página não pode ser inferior a 0 para pesquisa de reportes");

		if (size < 1)
			throw new ReportException("O tamanho da página não pode ser inferior a 1 para pesquisa de reportes");

		var userId = authenticatedUserGateway.getSubject();

		CanonicalContext.add("user_id", userId);

		if (userId == null || userId.isBlank())
			throw new NotAuthorizedException(
					"O ID do usuário não pode estar em branco para pesquisa de reportes. Verifique a autenticação.");

		return reportGateway.getLastReportsByUserId(userId, page, size);
	}

	/**
	 * Recupera os reportes mais recentes dos videos enviados pelo usuário
	 * autenticado.
	 *
	 * @return lista de {@link Report} (pode ser vazia)
	 */
	@WithSpan(name = "usecase.get.authenticated.user.all.reports")
	public List<Report> getAuthenticatedUserLastReports() {
		var userId = authenticatedUserGateway.getSubject();

		CanonicalContext.add("user_id", userId);

		if (userId == null || userId.isBlank())
			throw new NotAuthorizedException(
					"O ID do usuário não pode estar em branco para pesquisa de reportes. Verifique a autenticação.");

		return reportGateway.getLastReportsByUserId(userId);
	}
}
