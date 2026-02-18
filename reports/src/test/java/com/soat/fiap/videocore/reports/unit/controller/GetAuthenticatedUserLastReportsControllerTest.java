package com.soat.fiap.videocore.reports.unit.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthenticatedUserLastReportsController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

/** Testes unitários do {@link GetAuthenticatedUserLastReportsController}. */
class GetAuthenticatedUserLastReportsControllerTest {

	@Test
	void shouldReturnLastReportsFromAuthenticatedUser() {
		// Arrange
		GetAuthUserLastReportsUseCase useCase = mock(GetAuthUserLastReportsUseCase.class);
		ReportPresenter presenter = mock(ReportPresenter.class);

		var reports = List.of(mock(Report.class));
		var response = List.of(mock(ReportResponse.class));

		when(useCase.getAuthenticatedUserLastReports()).thenReturn(reports);
		when(presenter.toResponse(reports)).thenReturn(response);

		var controller = new GetAuthenticatedUserLastReportsController(useCase, presenter);

		// Act
		var result = controller.getAuthenticatedUserLastReports();

		// Assert
		assertSame(response, result);
		verify(useCase).getAuthenticatedUserLastReports();
		verify(presenter).toResponse(reports);
	}
}
