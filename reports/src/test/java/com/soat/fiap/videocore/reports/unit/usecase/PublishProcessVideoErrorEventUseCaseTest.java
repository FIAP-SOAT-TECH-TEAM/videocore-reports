package com.soat.fiap.videocore.reports.unit.usecase;

import com.soat.fiap.videocore.reports.core.application.usecase.PublishProcessVideoErrorEventUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.EventPublisherGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link PublishProcessVideoErrorEventUseCase}.
 */
class PublishProcessVideoErrorEventUseCaseTest {

    @Test
    void shouldPublishEventWhenReportIsValid() {
        // arrange
        EventPublisherGateway gateway = mock(EventPublisherGateway.class);
        PublishProcessVideoErrorEventUseCase useCase =
                new PublishProcessVideoErrorEventUseCase(gateway);
        Report report = mock(Report.class);
        when(report.getVideoName()).thenReturn("video");
        when(report.getUserId()).thenReturn("user");
        when(report.getRequestId()).thenReturn("request");
        when(report.getMinuteFrameCut()).thenReturn(1L);
        when(report.getPercentStatusProcess()).thenReturn(10.0);
        when(report.getReportTime()).thenReturn(java.time.Instant.now());

        // act
        useCase.publishProcessVideoErrorEvent(report);

        // assert
        verify(gateway).publishProcessVideoErrorEvent(any());
    }

    @Test
    void shouldThrowExceptionWhenReportIsNull() {
        // arrange
        EventPublisherGateway gateway = mock(EventPublisherGateway.class);
        PublishProcessVideoErrorEventUseCase useCase =
                new PublishProcessVideoErrorEventUseCase(gateway);

        // act + assert
        assertThrows(ReportException.class, () -> useCase.publishProcessVideoErrorEvent(null));
    }
}