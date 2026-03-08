package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

/** Testes unitários do {@link GetAuthUserLastReportsUseCase}. */
class GetAuthUserLastReportsUseCaseTest {

	@Test
	void shouldReturnPaginatedReportsWhenUserIsAuthenticated() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		int page = 0;
		int size = 10;

		when(userGateway.getSubject()).thenReturn("user");

		PaginationDTO<Report> pagination = mock(PaginationDTO.class);
		when(reportGateway.getLastReportsByUserId("user", page, size)).thenReturn(pagination);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act
		PaginationDTO<Report> result = useCase.getAuthenticatedUserLastReports(page, size);

		// Assert
		assertSame(pagination, result);
		verify(reportGateway).getLastReportsByUserId("user", page, size);
	}

	@Test
	void shouldReturnReportsWhenUserIsAuthenticatedWithoutPagination() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn("user");

		List<Report> reports = List.of(mock(Report.class));
		when(reportGateway.getLastReportsByUserId("user")).thenReturn(reports);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act
		List<Report> result = useCase.getAuthenticatedUserLastReports();

		// Assert
		assertSame(reports, result);
		verify(reportGateway).getLastReportsByUserId("user");
	}

	@Test
	void shouldThrowExceptionWhenUserIsNotAuthenticatedWithPagination() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn(" ");

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, () -> useCase.getAuthenticatedUserLastReports(0, 10));
	}

	@Test
	void shouldThrowExceptionWhenUserIsNotAuthenticatedWithoutPagination() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn(null);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, useCase::getAuthenticatedUserLastReports);
	}

	@Test
	void shouldThrowExceptionWhenPageIsNegative() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ReportException.class, () -> useCase.getAuthenticatedUserLastReports(-1, 10));

		verifyNoInteractions(reportGateway);
		verifyNoInteractions(userGateway);
	}

	@Test
	void shouldThrowExceptionWhenSizeIsLessThanOne() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ReportException.class, () -> useCase.getAuthenticatedUserLastReports(0, 0));

		verifyNoInteractions(reportGateway);
		verifyNoInteractions(userGateway);
	}
}
