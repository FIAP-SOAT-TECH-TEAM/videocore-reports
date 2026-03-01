package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthReportByIdUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ForbiddenException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportNotFoundException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

/** Testes unitários do {@link GetAuthReportByIdUseCase}. */
class GetAuthReportByIdUseCaseTest {

	@Test
	void shouldReturnReportWhenUserIsAuthorized() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		var report = mock(Report.class);
		when(userGateway.getSubject()).thenReturn("user");
		when(report.getUserId()).thenReturn("user");
		when(reportGateway.getById("report-id")).thenReturn(Optional.of(report));

		var useCase = new GetAuthReportByIdUseCase(reportGateway, userGateway);

		// Act
		Report result = useCase.getReportById("report-id");

		// Assert
		assertSame(report, result);
	}

	@Test
	void shouldThrowNotAuthorizedWhenUserIsBlank() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);
		when(userGateway.getSubject()).thenReturn(" ");

		var useCase = new GetAuthReportByIdUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, () -> useCase.getReportById("report-id"));
	}

	@Test
	void shouldThrowReportExceptionWhenReportIdIsBlank() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);
		when(userGateway.getSubject()).thenReturn("user");

		var useCase = new GetAuthReportByIdUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ReportException.class, () -> useCase.getReportById(" "));
	}

	@Test
	void shouldThrowReportNotFoundWhenReportDoesNotExist() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);
		when(userGateway.getSubject()).thenReturn("user");
		when(reportGateway.getById("report-id")).thenReturn(Optional.empty());

		var useCase = new GetAuthReportByIdUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ReportNotFoundException.class, () -> useCase.getReportById("report-id"));
	}

	@Test
	void shouldThrowForbiddenWhenUserDoesNotOwnReport() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);
		var report = mock(Report.class);
		when(userGateway.getSubject()).thenReturn("user");
		when(report.getUserId()).thenReturn("other-user");
		when(reportGateway.getById("report-id")).thenReturn(Optional.of(report));

		var useCase = new GetAuthReportByIdUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ForbiddenException.class, () -> useCase.getReportById("report-id"));
	}
}
