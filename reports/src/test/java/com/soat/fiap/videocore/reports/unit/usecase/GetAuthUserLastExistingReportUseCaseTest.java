package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastExistingReportUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ForbiddenException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportNotFoundException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

/** Testes unitários do {@link GetAuthUserLastExistingReportUseCase}. */
class GetAuthUserLastExistingReportUseCaseTest {

	@Test
	void shouldReturnReportWhenUserIsAuthorized() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		var report = mock(Report.class);

		String userId = "user";
		String requestId = "request-id";
		String videoName = "video.mp4";

		when(userGateway.getSubject()).thenReturn(userId);
		when(report.getUserId()).thenReturn(userId);
		when(reportGateway.getLastExistingReport(userId, requestId, videoName)).thenReturn(Optional.of(report));

		var useCase = new GetAuthUserLastExistingReportUseCase(reportGateway, userGateway);

		// Act
		Report result = useCase.getAuthUserLastExistingReport(requestId, videoName);

		// Assert
		assertSame(report, result);
	}

	@Test
	void shouldThrowNotAuthorizedWhenUserIsBlank() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn(" ");

		var useCase = new GetAuthUserLastExistingReportUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class,
				() -> useCase.getAuthUserLastExistingReport("request-id", "video.mp4"));
	}

	@Test
	void shouldThrowReportExceptionWhenRequestIdIsBlank() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn("user");

		var useCase = new GetAuthUserLastExistingReportUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ReportException.class, () -> useCase.getAuthUserLastExistingReport(" ", "video.mp4"));
	}

	@Test
	void shouldThrowReportExceptionWhenVideoNameIsBlank() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		when(userGateway.getSubject()).thenReturn("user");

		var useCase = new GetAuthUserLastExistingReportUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ReportException.class, () -> useCase.getAuthUserLastExistingReport("request-id", " "));
	}

	@Test
	void shouldThrowReportNotFoundWhenReportDoesNotExist() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		String userId = "user";
		String requestId = "request-id";
		String videoName = "video.mp4";

		when(userGateway.getSubject()).thenReturn(userId);
		when(reportGateway.getLastExistingReport(userId, requestId, videoName)).thenReturn(Optional.empty());

		var useCase = new GetAuthUserLastExistingReportUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ReportNotFoundException.class, () -> useCase.getAuthUserLastExistingReport(requestId, videoName));
	}

	@Test
	void shouldThrowForbiddenWhenUserDoesNotOwnReport() {
		// Arrange
		ReportGateway reportGateway = mock(ReportGateway.class);
		AuthenticatedUserGateway userGateway = mock(AuthenticatedUserGateway.class);

		var report = mock(Report.class);

		String userId = "user";
		String requestId = "request-id";
		String videoName = "video.mp4";

		when(userGateway.getSubject()).thenReturn(userId);
		when(report.getUserId()).thenReturn("other-user");
		when(reportGateway.getLastExistingReport(userId, requestId, videoName)).thenReturn(Optional.of(report));

		var useCase = new GetAuthUserLastExistingReportUseCase(reportGateway, userGateway);

		// Act & Assert
		assertThrows(ForbiddenException.class, () -> useCase.getAuthUserLastExistingReport(requestId, videoName));
	}
}
