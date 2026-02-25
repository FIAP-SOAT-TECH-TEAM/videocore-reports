package com.soat.fiap.videocore.reports.core.application.usecase;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ForbiddenException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportNotFoundException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

import lombok.RequiredArgsConstructor;

/**
 * Caso de uso responsável por buscar um {@link Report} pelo seu identificador
 * único.
 */
@Component @RequiredArgsConstructor
public class GetReportByIdUseCase {

	private final ReportGateway reportGateway;
	private final AuthenticatedUserGateway authenticatedUserGateway;

	/**
	 * Busca um {@link Report} existente pelo seu {@code reportId}.
	 *
	 * @param reportId
	 *            identificador do reporte
	 * @return {@link Optional} com o {@link Report} encontrado, ou vazio se não
	 *         existir
	 */
	@WithSpan(name = "usecase.get.report.by.id")
	public Report getReportById(String reportId) {
		var userId = authenticatedUserGateway.getSubject();

		CanonicalContext.add("user_id", userId);

		if (userId == null || userId.isBlank())
			throw new NotAuthorizedException(
					"O ID do usuário não pode estar em branco para pesquisa de reportes. Verifique a autenticação.");

		if (reportId == null || reportId.isBlank())
			throw new ReportException("O ID do reporte não pode ser nulo ou vazio para sua pesquisa");

		var report = reportGateway.getById(reportId);

		if (report.isEmpty())
			throw new ReportNotFoundException("Reporte não encontrado");

		if (!report.get().getUserId().equals(userId))
			throw new ForbiddenException("Usuário não autorizado a realizar esta ação");

		return report.get();
	}
}
