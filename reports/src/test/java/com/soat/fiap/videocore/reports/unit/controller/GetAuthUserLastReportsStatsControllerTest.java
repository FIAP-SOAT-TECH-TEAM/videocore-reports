package com.soat.fiap.videocore.reports.unit.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.output.ReportsStatsOutput;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsStatsUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserLastReportsStatsController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportsStatsResponse;

/** Testes unitários do {@link GetAuthUserLastReportsStatsController}. */
class GetAuthUserLastReportsStatsControllerTest {

	@Test
	void shouldReturnReportsStats() {
		// Arrange
		GetAuthUserLastReportsStatsUseCase useCase = mock(GetAuthUserLastReportsStatsUseCase.class);
		ReportPresenter presenter = mock(ReportPresenter.class);

		var statsOutput = mock(ReportsStatsOutput.class);
		var response = mock(ReportsStatsResponse.class);

		when(useCase.getAuthUserLastReportsStats()).thenReturn(statsOutput);
		when(presenter.toStatsResponse(statsOutput)).thenReturn(response);

		var controller = new GetAuthUserLastReportsStatsController(useCase, presenter);

		// Act
		var result = controller.getAuthUserLastReportsStats();

		// Assert
		assertSame(response, result);
		verify(useCase).getAuthUserLastReportsStats();
		verify(presenter).toStatsResponse(statsOutput);
	}
}
