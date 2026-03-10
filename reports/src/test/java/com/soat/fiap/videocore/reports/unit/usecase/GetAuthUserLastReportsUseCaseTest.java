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
		String orderField = "reportTime";
		String orderDirection = "DESC";

		when(userGateway.getSubject()).thenReturn("user");

		PaginationDTO<Report> pagination = mock(PaginationDTO.class);
		when(reportGateway.getLastReportsByUserId("user", page, size, orderField, orderDirection))
				.thenReturn(pagination);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act
		PaginationDTO<Report> result = useCase.getAuthenticatedUserLastReports(page, size, orderField, orderDirection);

		// Assert
		assertSame(pagination, result);
		verify(reportGateway).getLastReportsByUserId("user", page, size, orderField, orderDirection);
	}

	@Test
	void shouldReturnReportsWhenUserIsAuthenticatedWithoutPagination() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		String orderField = "reportTime";
		String orderDirection = "DESC";

		when(userGateway.getSubject()).thenReturn("user");

		List<Report> reports = List.of(mock(Report.class));
		when(reportGateway.getLastReportsByUserId("user", orderField, orderDirection)).thenReturn(reports);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act
		List<Report> result = useCase.getAuthenticatedUserLastReports(orderField, orderDirection);

		// Assert
		assertSame(reports, result);
		verify(reportGateway).getLastReportsByUserId("user", orderField, orderDirection);
	}

	@Test
	void shouldThrowExceptionWhenUserIsNotAuthenticatedWithPagination() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn(" ");

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class,
				() -> useCase.getAuthenticatedUserLastReports(0, 10, "reportTime", "DESC"));
	}

	@Test
	void shouldThrowExceptionWhenUserIsNotAuthenticatedWithoutPagination() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn(null);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, () -> useCase.getAuthenticatedUserLastReports("reportTime", "DESC"));
	}

	@Test
	void shouldThrowExceptionWhenPageIsNegative() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		var useCase = new GetAuthUserLastReportsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ReportException.class,
				() -> useCase.getAuthenticatedUserLastReports(-1, 10, "reportTime", "DESC"));

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
		assertThrows(ReportException.class, () -> useCase.getAuthenticatedUserLastReports(0, 0, "reportTime", "DESC"));

		verifyNoInteractions(reportGateway);
		verifyNoInteractions(userGateway);
	}
}
