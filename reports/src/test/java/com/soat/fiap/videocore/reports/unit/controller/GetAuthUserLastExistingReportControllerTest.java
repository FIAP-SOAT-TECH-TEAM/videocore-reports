package com.soat.fiap.videocore.reports.unit.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastExistingReportUseCase;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserLastExistingReportController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

/** Testes unitários do {@link GetAuthUserLastExistingReportController}. */
class GetAuthUserLastExistingReportControllerTest {

	@Test
	void shouldReturnLastExistingReport() {
		// Arrange
		GetAuthUserLastExistingReportUseCase useCase = mock(GetAuthUserLastExistingReportUseCase.class);
		ReportPresenter presenter = mock(ReportPresenter.class);

		var report = mock(Report.class);
		var response = mock(ReportResponse.class);

		String requestId = "request-id";
		String videoName = "video.mp4";

		when(useCase.getAuthUserLastExistingReport(requestId, videoName)).thenReturn(report);
		when(presenter.toResponse(report)).thenReturn(response);

		var controller = new GetAuthUserLastExistingReportController(useCase, presenter);

		// Act
		var result = controller.getAuthUserLastExistingReport(requestId, videoName);

		// Assert
		assertSame(response, result);
		verify(useCase).getAuthUserLastExistingReport(requestId, videoName);
		verify(presenter).toResponse(report);
	}
}
