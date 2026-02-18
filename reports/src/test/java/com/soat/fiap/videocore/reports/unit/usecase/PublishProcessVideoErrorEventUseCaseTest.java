package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.PublishProcessVideoErrorEventUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.EventPublisherGateway;

/** Testes unitários do {@link PublishProcessVideoErrorEventUseCase}. */
class PublishProcessVideoErrorEventUseCaseTest {

	@Test
	void shouldPublishEventWhenReportIsValid() {
		// Arrange
		EventPublisherGateway gateway = mock(EventPublisherGateway.class);
		PublishProcessVideoErrorEventUseCase useCase = new PublishProcessVideoErrorEventUseCase(gateway);
		Report report = mock(Report.class);
		when(report.getVideoName()).thenReturn("video");
		when(report.getUserId()).thenReturn("user");
		when(report.getRequestId()).thenReturn("request");
		when(report.getMinuteFrameCut()).thenReturn(1L);
		when(report.getPercentStatusProcess()).thenReturn(10.0);
		when(report.getReportTime()).thenReturn(java.time.Instant.now());

		// Act
		useCase.publishProcessVideoErrorEvent(report);

		// Assert
		verify(gateway).publishProcessVideoErrorEvent(any());
	}

	@Test
	void shouldThrowExceptionWhenReportIsNull() {
		// Arrange
		EventPublisherGateway gateway = mock(EventPublisherGateway.class);
		PublishProcessVideoErrorEventUseCase useCase = new PublishProcessVideoErrorEventUseCase(gateway);

		// Act & Assert
		assertThrows(ReportException.class, () -> useCase.publishProcessVideoErrorEvent(null));
	}
}
