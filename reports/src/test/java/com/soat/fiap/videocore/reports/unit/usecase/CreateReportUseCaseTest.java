package com.soat.fiap.videocore.reports.unit.usecase;

import com.soat.fiap.videocore.reports.core.application.input.ReportInput;
import com.soat.fiap.videocore.reports.core.application.usecase.CreateReportUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link CreateReportUseCase}.
 */
class CreateReportUseCaseTest {

    private final CreateReportUseCase useCase = new CreateReportUseCase();

    @Test
    void shouldCreateReportWithStartedStatusWhenPercentIsZero() {
        // Arrange
        ReportInput input = mock(ReportInput.class);
        when(input.videoName()).thenReturn("video.mp4");
        when(input.imageMinute()).thenReturn(0L);
        when(input.frameCutMinutes()).thenReturn(1L);
        when(input.userId()).thenReturn("user");
        when(input.requestId()).thenReturn("request");
        when(input.percentStatusProcess()).thenReturn(0.0);
        when(input.reportTime()).thenReturn(Instant.now());
        when(input.isError()).thenReturn(false);

        // Act
        Report report = useCase.createReport(input);

        // Assert
        assertEquals(ProcessStatus.STARTED, report.getStatus());
    }

    @Test
    void shouldCreateReportWithProcessingStatusWhenPercentBetweenZeroAndHundred() {
        // Arrange
        ReportInput input = mock(ReportInput.class);
        when(input.videoName()).thenReturn("video.mp4");
        when(input.imageMinute()).thenReturn(1L);
        when(input.frameCutMinutes()).thenReturn(1L);
        when(input.userId()).thenReturn("user");
        when(input.requestId()).thenReturn("request");
        when(input.percentStatusProcess()).thenReturn(50.0);
        when(input.reportTime()).thenReturn(Instant.now());
        when(input.isError()).thenReturn(false);

        // Act
        Report report = useCase.createReport(input);

        // Assert
        assertEquals(ProcessStatus.PROCESSING, report.getStatus());
    }

    @Test
    void shouldCreateReportWithCompletedStatusWhenPercentIsHundred() {
        // Arrange
        ReportInput input = mock(ReportInput.class);
        when(input.videoName()).thenReturn("video.mp4");
        when(input.imageMinute()).thenReturn(1L);
        when(input.frameCutMinutes()).thenReturn(1L);
        when(input.userId()).thenReturn("user");
        when(input.requestId()).thenReturn("request");
        when(input.percentStatusProcess()).thenReturn(100.0);
        when(input.reportTime()).thenReturn(Instant.now());
        when(input.isError()).thenReturn(false);

        // Act
        Report report = useCase.createReport(input);

        // Assert
        assertEquals(ProcessStatus.COMPLETED, report.getStatus());
    }

    @Test
    void shouldCreateReportWithFailedStatusWhenInputHasError() {
        // Arrange
        ReportInput input = mock(ReportInput.class);
        when(input.videoName()).thenReturn("video.mp4");
        when(input.imageMinute()).thenReturn(1L);
        when(input.frameCutMinutes()).thenReturn(1L);
        when(input.userId()).thenReturn("user");
        when(input.requestId()).thenReturn("request");
        when(input.percentStatusProcess()).thenReturn(10.0);
        when(input.reportTime()).thenReturn(Instant.now());
        when(input.isError()).thenReturn(true);

        // Act
        Report report = useCase.createReport(input);

        // Assert
        assertEquals(ProcessStatus.FAILED, report.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenInputIsNull() {
        // Arrange
        ReportInput input = null;

        // Act & Assert
        assertThrows(ReportException.class, () -> useCase.createReport(input));
    }
}