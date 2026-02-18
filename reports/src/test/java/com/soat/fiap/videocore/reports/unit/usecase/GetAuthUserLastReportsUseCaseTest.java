package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

/** Testes unitários do {@link GetAuthUserLastReportsUseCase}. */
class GetAuthUserLastReportsUseCaseTest {

	@Test
	void shouldReturnReportsWhenUserIsAuthenticated() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);
		when(userGateway.getSubject()).thenReturn("user");
		when(reportGateway.getLastReportsByUserId("user")).thenReturn(List.of(mock(Report.class)));

		GetAuthUserLastReportsUseCase useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act
		List<Report> reports = useCase.getAuthenticatedUserLastReports();

		// Assert
		assertEquals(1, reports.size());
	}

	@Test
	void shouldThrowExceptionWhenUserIsNotAuthenticated() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);
		when(userGateway.getSubject()).thenReturn(" ");

		GetAuthUserLastReportsUseCase useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, useCase::getAuthenticatedUserLastReports);
	}
}
