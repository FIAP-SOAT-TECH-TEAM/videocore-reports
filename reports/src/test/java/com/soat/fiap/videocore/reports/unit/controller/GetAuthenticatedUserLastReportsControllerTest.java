package com.soat.fiap.videocore.reports.unit.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserLastReportsUseCase;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthenticatedUserLastReportsController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ReportPresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.PaginationResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

/** Testes unitários do {@link GetAuthenticatedUserLastReportsController}. */
class GetAuthenticatedUserLastReportsControllerTest {

	@Test
	void shouldReturnLastReportsFromAuthenticatedUser() {
		// Arrange
		GetAuthUserLastReportsUseCase useCase = mock(GetAuthUserLastReportsUseCase.class);
		ReportPresenter presenter = mock(ReportPresenter.class);

		int page = 0;
		int size = 10;

		PaginationDTO<Report> paginationDTO = mock(PaginationDTO.class);
		PaginationResponse<ReportResponse> response = mock(PaginationResponse.class);

		when(useCase.getAuthenticatedUserLastReports(page, size)).thenReturn(paginationDTO);
		when(presenter.toPaginationResponse(paginationDTO)).thenReturn(response);

		var controller = new GetAuthenticatedUserLastReportsController(useCase, presenter);

		// Act
		var result = controller.getAuthenticatedUserLastReports(page, size);

		// Assert
		assertSame(response, result);
		verify(useCase).getAuthenticatedUserLastReports(page, size);
		verify(presenter).toPaginationResponse(paginationDTO);
	}
}
