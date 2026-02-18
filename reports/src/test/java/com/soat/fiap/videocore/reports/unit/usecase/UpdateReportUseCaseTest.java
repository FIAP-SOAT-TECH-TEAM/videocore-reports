package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.UpdateReportUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.Metadata;
import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;

/** Testes unitários do {@link UpdateReportUseCase}. */
class UpdateReportUseCaseTest {

	@Test
	void shouldUpdateReportSuccessfully() {
		// Arrange
		UpdateReportUseCase useCase = new UpdateReportUseCase();
		Report report = mock(Report.class);
		Report newReport = mock(Report.class);

		when(newReport.getVideoName()).thenReturn("video");
		when(newReport.getImageMinute()).thenReturn(1L);
		when(newReport.getMinuteFrameCut()).thenReturn(1L);
		when(newReport.getPercentStatusProcess()).thenReturn(10.0);
		when(newReport.getMetadata()).thenReturn(mock(Metadata.class));
		when(newReport.getReportTime()).thenReturn(java.time.Instant.now());
		when(newReport.getStatus()).thenReturn(ProcessStatus.PROCESSING);

		// Act
		Report result = useCase.updateReport(report, newReport);

		// Assert
		assertEquals(report, result);
	}

	@Test
	void shouldThrowExceptionWhenAnyReportIsNull() {
		// Arrange
		UpdateReportUseCase useCase = new UpdateReportUseCase();

		// Act & Assert
		assertThrows(ReportException.class, () -> useCase.updateReport(null, mock(Report.class)));
		assertThrows(ReportException.class, () -> useCase.updateReport(mock(Report.class), null));
	}
}
