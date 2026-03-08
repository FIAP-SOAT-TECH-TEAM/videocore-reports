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
 * Caso de uso responsável por buscar o último {@link Report} existente de um
 * vídeo enviado pelo usuário autenticado.
 */
@Component @RequiredArgsConstructor
public class GetAuthUserLastExistingReportUseCase {

	private final ReportGateway reportGateway;
	private final AuthenticatedUserGateway authenticatedUserGateway;

	/**
	 * Busca o último {@link Report} existente de um vídeo enviado pelo usuário
	 * autenticado, por meio do ID da requisição e do nome do vídeo.
	 *
	 * @param requestId
	 *            identificador da requisição
	 * @param videoName
	 *            nome do vídeo
	 * @return {@link Optional} com o {@link Report} encontrado, ou vazio se não
	 *         existir
	 */
	@WithSpan(name = "usecase.get.last.existing.report")
	public Report getAuthUserLastExistingReport(String requestId, String videoName) {
		var userId = authenticatedUserGateway.getSubject();

		CanonicalContext.add("user_id", userId);

		if (userId == null || userId.isBlank())
			throw new NotAuthorizedException(
					"O ID do usuário não pode estar em branco para pesquisa de reportes. Verifique a autenticação.");

		if (requestId == null || requestId.isBlank())
			throw new ReportException("O ID da requisição não pode ser nulo ou vazio para pesquisa de reporte");

		if (videoName == null || videoName.isBlank())
			throw new ReportException("O nome do vídeo não pode ser nulo ou vazio para pesquisa de reporte");

		var report = reportGateway.getLastExistingReport(userId, requestId, videoName);

		if (report.isEmpty())
			throw new ReportNotFoundException("Reporte não encontrado");

		if (!report.get().getUserId().equals(userId))
			throw new ForbiddenException("Usuário não autorizado a realizar esta ação");

		return report.get();
	}
}
