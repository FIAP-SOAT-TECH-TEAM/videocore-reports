package com.soat.fiap.videocore.reports.unit.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthReportByIdUseCase;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthReportByIdController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

/** Testes unitários do {@link GetAuthReportByIdController}. */
class GetAuthReportByIdControllerTest {

	@Test
	void shouldReturnReportById() {
		// Arrange
		GetAuthReportByIdUseCase useCase = mock(GetAuthReportByIdUseCase.class);
		ReportPresenter presenter = mock(ReportPresenter.class);

		var report = mock(Report.class);
		var response = mock(ReportResponse.class);

		when(useCase.getReportById("report-id")).thenReturn(report);
		when(presenter.toResponse(report)).thenReturn(response);

		var controller = new GetAuthReportByIdController(useCase, presenter);

		// Act
		var result = controller.getReportById("report-id");

		// Assert
		assertSame(response, result);
		verify(useCase).getReportById("report-id");
		verify(presenter).toResponse(report);
	}
}
