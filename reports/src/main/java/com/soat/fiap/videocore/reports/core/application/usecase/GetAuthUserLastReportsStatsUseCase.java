package com.soat.fiap.videocore.reports.core.application.usecase;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.output.ReportsStatsOutput;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

import lombok.RequiredArgsConstructor;

/**
 * Use case responsável por recuperar estatísticas dos reportes mais recentes
 * dos vídeos enviados pelo usuário autenticado.
 */
@Component @RequiredArgsConstructor
public class GetAuthUserLastReportsStatsUseCase {

	private final ReportGateway reportGateway;
	private final AuthenticatedUserGateway authenticatedUserGateway;

	/**
	 * Recupera estatísticas dos reportes mais recentes dos vídeos enviados pelo
	 * usuário autenticado.
	 *
	 * @return objeto contendo estatísticas agregadas dos reportes do usuário
	 */
	@WithSpan(name = "usecase.get.authenticated.user.reports.stats")
	public ReportsStatsOutput getAuthUserLastReportsStats() {
		var userId = authenticatedUserGateway.getSubject();

		CanonicalContext.add("user_id", userId);

		if (userId == null || userId.isBlank())
			throw new NotAuthorizedException(
					"O ID do usuário não pode estar em branco para pesquisa de reportes. Verifique a autenticação.");

		var status = reportGateway.getLastReportsStatusByUserId(userId);
		var total = (long) status.size();
		var completed = status.stream().filter(s -> s == ProcessStatus.COMPLETED).count();
		var processing = status.stream()
				.filter(s -> s == ProcessStatus.STARTED || s == ProcessStatus.PROCESSING)
				.count();
		var failed = status.stream().filter(s -> s == ProcessStatus.FAILED).count();

		var statsObject = new ReportsStatsOutput(total, completed, processing, failed);

		CanonicalContext.add("stats_object", statsObject);

		return statsObject;
	}
}
