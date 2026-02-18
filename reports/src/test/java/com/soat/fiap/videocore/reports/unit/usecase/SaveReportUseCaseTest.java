package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.SaveReportUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ProcessReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;

/** Testes unitários do {@link SaveReportUseCase}. */
class SaveReportUseCaseTest {

	@Test
	void shouldSaveReportSuccessfully() {
		// Arrange
		ReportGateway gateway = mock(ReportGateway.class);
		Report report = mock(Report.class);
		when(gateway.save(report)).thenReturn(report);

		SaveReportUseCase useCase = new SaveReportUseCase(gateway);

		// Act
		Report saved = useCase.saveReport(report);

		// Assert
		assertEquals(report, saved);
	}

	@Test
	void shouldThrowExceptionWhenReportIsNull() {
		// Arrange
		ReportGateway gateway = mock(ReportGateway.class);
		SaveReportUseCase useCase = new SaveReportUseCase(gateway);

		// Act & Assert
		assertThrows(ProcessReportException.class, () -> useCase.saveReport(null));
	}

	@Test
	void shouldWrapExceptionWhenGatewayThrowsError() {
		// Arrange
		ReportGateway gateway = mock(ReportGateway.class);
		Report report = mock(Report.class);
		when(gateway.save(report)).thenThrow(new RuntimeException("db error"));

		SaveReportUseCase useCase = new SaveReportUseCase(gateway);

		// Act & Assert
		assertThrows(ProcessReportException.class, () -> useCase.saveReport(report));
	}
}
