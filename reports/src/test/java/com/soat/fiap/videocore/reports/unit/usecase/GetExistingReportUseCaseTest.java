package com.soat.fiap.videocore.reports.unit.usecase;

import com.soat.fiap.videocore.reports.core.application.usecase.GetExistingReportUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetExistingReportUseCase}.
 */
class GetExistingReportUseCaseTest {

    @Test
    void shouldReturnExistingReportWhenAllParametersAreValid() {
        // arrange
        ReportGateway gateway = mock(ReportGateway.class);
        GetExistingReportUseCase useCase = new GetExistingReportUseCase(gateway);

        Report report = mock(Report.class);
        when(gateway.getExistingReport("user", "request", "video.mp4", 50.0))
                .thenReturn(Optional.of(report));

        // act
        Optional<Report> result =
                useCase.getExistingReport("user", "request", "video.mp4", 50.0);

        // assert
        assertTrue(result.isPresent());
        assertEquals(report, result.get());
    }

    @Test
    void shouldReturnEmptyOptionalWhenReportDoesNotExist() {
        // arrange
        ReportGateway gateway = mock(ReportGateway.class);
        GetExistingReportUseCase useCase = new GetExistingReportUseCase(gateway);

        when(gateway.getExistingReport("user", "request", "video.mp4", 10.0))
                .thenReturn(Optional.empty());

        // act
        Optional<Report> result =
                useCase.getExistingReport("user", "request", "video.mp4", 10.0);

        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        // arrange
        ReportGateway gateway = mock(ReportGateway.class);
        GetExistingReportUseCase useCase = new GetExistingReportUseCase(gateway);

        // act + assert
        assertThrows(
                ReportException.class,
                () -> useCase.getExistingReport(null, "request", "video.mp4", 10.0)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsBlank() {
        // arrange
        ReportGateway gateway = mock(ReportGateway.class);
        GetExistingReportUseCase useCase = new GetExistingReportUseCase(gateway);

        // act + assert
        assertThrows(
                ReportException.class,
                () -> useCase.getExistingReport(" ", "request", "video.mp4", 10.0)
        );
    }

    @Test
    void shouldThrowExceptionWhenRequestIdIsNull() {
        // arrange
        ReportGateway gateway = mock(ReportGateway.class);
        GetExistingReportUseCase useCase = new GetExistingReportUseCase(gateway);

        // act + assert
        assertThrows(
                ReportException.class,
                () -> useCase.getExistingReport("user", null, "video.mp4", 10.0)
        );
    }

    @Test
    void shouldThrowExceptionWhenRequestIdIsBlank() {
        // arrange
        ReportGateway gateway = mock(ReportGateway.class);
        GetExistingReportUseCase useCase = new GetExistingReportUseCase(gateway);

        // act + assert
        assertThrows(
                ReportException.class,
                () -> useCase.getExistingReport("user", " ", "video.mp4", 10.0)
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsNull() {
        // arrange
        ReportGateway gateway = mock(ReportGateway.class);
        GetExistingReportUseCase useCase = new GetExistingReportUseCase(gateway);

        // act + assert
        assertThrows(
                ReportException.class,
                () -> useCase.getExistingReport("user", "request", null, 10.0)
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsBlank() {
        // arrange
        ReportGateway gateway = mock(ReportGateway.class);
        GetExistingReportUseCase useCase = new GetExistingReportUseCase(gateway);

        // act + assert
        assertThrows(
                ReportException.class,
                () -> useCase.getExistingReport("user", "request", " ", 10.0)
        );
    }
}