package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

/** Testes unitários do {@link GetAuthUserLastReportsUseCase}. */
class GetAuthUserLastReportsUseCaseTest {

	@Test
	void shouldReturnReportsWhenUserIsAuthenticated() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		int page = 0;
		int size = 10;

		when(userGateway.getSubject()).thenReturn("user");

		PaginationDTO<Report> pagination = mock(PaginationDTO.class);

		when(reportGateway.getLastReportsByUserId("user", page, size)).thenReturn(pagination);

		GetAuthUserLastReportsUseCase useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act
		PaginationDTO<Report> result = useCase.getAuthenticatedUserLastReports(page, size);

		// Assert
		assertSame(pagination, result);
		verify(reportGateway).getLastReportsByUserId("user", page, size);
	}

	@Test
	void shouldThrowExceptionWhenUserIsNotAuthenticated() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn(" ");

		GetAuthUserLastReportsUseCase useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, () -> useCase.getAuthenticatedUserLastReports(0, 10));
	}
}
