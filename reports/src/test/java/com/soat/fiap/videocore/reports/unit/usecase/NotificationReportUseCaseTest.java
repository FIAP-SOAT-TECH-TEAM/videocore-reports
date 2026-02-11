package com.soat.fiap.videocore.reports.unit.usecase;

import com.soat.fiap.videocore.reports.core.application.usecase.NotificationReportUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.NotificationGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link NotificationReportUseCase}.
 */
class NotificationReportUseCaseTest {

    @Test
    void shouldNotifyWhenReportIsValid() {
        // Arrange
        var gateway = mock(NotificationGateway.class);
        NotificationReportUseCase useCase = new NotificationReportUseCase(gateway);
        Report report = mock(Report.class);

        // Act
        useCase.notificationReport(report);

        // Assert
        verify(gateway).notificateReportClients(report);
    }

    @Test
    void shouldThrowExceptionWhenReportIsNull() {
        // Arrange
        var gateway = mock(NotificationGateway.class);
        NotificationReportUseCase useCase = new NotificationReportUseCase(gateway);

        // Act & Assert
        assertThrows(ReportException.class, () -> useCase.notificationReport(null));
    }
}