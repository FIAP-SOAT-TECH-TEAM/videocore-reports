package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.output.ReportsStatsOutput;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsStatsUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

/** Testes unitários do {@link GetAuthUserLastReportsStatsUseCase}. */
class GetAuthUserLastReportsStatsUseCaseTest {

	@Test
	void shouldReturnStatsWhenUserIsAuthorized() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		String userId = "user";

		List<ProcessStatus> statuses = List.of(ProcessStatus.COMPLETED, ProcessStatus.COMPLETED, ProcessStatus.STARTED,
				ProcessStatus.PROCESSING, ProcessStatus.FAILED);

		when(userGateway.getSubject()).thenReturn(userId);
		when(reportGateway.getLastReportsStatusByUserId(userId)).thenReturn(statuses);

		var useCase = new GetAuthUserLastReportsStatsUseCase(reportGateway, userGateway);

		// Act
		ReportsStatsOutput result = useCase.getAuthUserLastReportsStats();

		// Assert
		assertNotNull(result);
		assertEquals(5, result.total());
		assertEquals(2, result.completed());
		assertEquals(2, result.processing());
		assertEquals(1, result.failed());
	}

	@Test
	void shouldThrowNotAuthorizedWhenUserIsNull() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn(null);

		var useCase = new GetAuthUserLastReportsStatsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, useCase::getAuthUserLastReportsStats);
	}

	@Test
	void shouldThrowNotAuthorizedWhenUserIsBlank() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn(" ");

		var useCase = new GetAuthUserLastReportsStatsUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, useCase::getAuthUserLastReportsStats);
	}
}
