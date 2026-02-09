package com.soat.fiap.videocore.reports.unit.model;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.*;
import com.soat.fiap.videocore.reports.fixtures.ReportFixture;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários da entidade {@link Report} com cobertura total.
 */
class ReportTest {

    @Test
    void shouldCreateReportWithAllValidValues() {
        // Arrange
        Report report = ReportFixture.validReport();

        // Act
        String videoName = report.getVideoName();
        long imageMinute = report.getImageMinute();
        long minuteFrameCut = report.getMinuteFrameCut();
        Double percent = report.getPercentStatusProcess();

        // Assert
        assertEquals("video.mp4", videoName);
        assertEquals(1L, imageMinute);
        assertEquals(1L, minuteFrameCut);
        assertEquals(10.0, percent);
    }

    @Test
    void shouldThrowExceptionWhenAnyConstructorArgumentIsNull() {
        // Arrange
        Instant now = Instant.now();

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new Report(null, ReportFixture.imageMinute(), ReportFixture.minuteFrameCut(),
                        ReportFixture.metadata(), ReportFixture.percentStatusProcess(), now, ProcessStatus.PROCESSING)
        );

        assertThrows(NullPointerException.class, () ->
                new Report(ReportFixture.videoName(), null, ReportFixture.minuteFrameCut(),
                        ReportFixture.metadata(), ReportFixture.percentStatusProcess(), now, ProcessStatus.PROCESSING)
        );

        assertThrows(NullPointerException.class, () ->
                new Report(ReportFixture.videoName(), ReportFixture.imageMinute(), null,
                        ReportFixture.metadata(), ReportFixture.percentStatusProcess(), now, ProcessStatus.PROCESSING)
        );

        assertThrows(NullPointerException.class, () ->
                new Report(ReportFixture.videoName(), ReportFixture.imageMinute(), ReportFixture.minuteFrameCut(),
                        null, ReportFixture.percentStatusProcess(), now, ProcessStatus.PROCESSING)
        );

        assertThrows(NullPointerException.class, () ->
                new Report(ReportFixture.videoName(), ReportFixture.imageMinute(), ReportFixture.minuteFrameCut(),
                        ReportFixture.metadata(), null, now, ProcessStatus.PROCESSING)
        );

        assertThrows(NullPointerException.class, () ->
                new Report(ReportFixture.videoName(), ReportFixture.imageMinute(), ReportFixture.minuteFrameCut(),
                        ReportFixture.metadata(), ReportFixture.percentStatusProcess(), null, ProcessStatus.PROCESSING)
        );

        assertThrows(NullPointerException.class, () ->
                new Report(ReportFixture.videoName(), ReportFixture.imageMinute(), ReportFixture.minuteFrameCut(),
                        ReportFixture.metadata(), ReportFixture.percentStatusProcess(), now, null)
        );
    }

    @Test
    void shouldSetIdSuccessfully() {
        // Arrange
        Report report = ReportFixture.validReport();

        // Act
        report.setId("id-123");

        // Assert
        assertEquals("id-123", report.getId());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        // Arrange
        Report report = ReportFixture.validReport();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> report.setId(null));
    }

    @Test
    void shouldThrowExceptionWhenIdIsBlank() {
        // Arrange
        Report report = ReportFixture.validReport();

        // Act & Assert
        assertThrows(ReportException.class, () -> report.setId(" "));
    }

    @Test
    void shouldUpdateAllSettersSuccessfully() {
        // Arrange
        Report report = ReportFixture.validReport();
        VideoName newVideoName = new VideoName("new-video.mp4");
        ImageMinute newImageMinute = new ImageMinute(5);
        MinuteFrameCut newMinuteFrameCut = new MinuteFrameCut(2);
        Metadata newMetadata = new Metadata("user-2", "request-2", "trace-2");
        PercentStatusProcess newPercent = new PercentStatusProcess(50.0);
        Instant newTime = Instant.now();

        // Act
        report.setVideoName(newVideoName);
        report.setImageMinute(newImageMinute);
        report.setMinuteFrameCut(newMinuteFrameCut);
        report.setMetadata(newMetadata);
        report.setPercentStatusProcess(newPercent);
        report.setReportTime(newTime);
        report.setStatus(ProcessStatus.COMPLETED);

        // Assert
        assertEquals("new-video.mp4", report.getVideoName());
        assertEquals(5L, report.getImageMinute());
        assertEquals(2L, report.getMinuteFrameCut());
        assertEquals("user-2", report.getUserId());
        assertEquals("request-2", report.getRequestId());
        assertEquals(50.0, report.getPercentStatusProcess());
        assertEquals(ProcessStatus.COMPLETED, report.getStatus());
        assertEquals(newTime, report.getReportTime());
    }

    @Test
    void shouldThrowExceptionWhenSetterReceivesNull() {
        // Arrange
        Report report = ReportFixture.validReport();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> report.setVideoName(null));
        assertThrows(NullPointerException.class, () -> report.setImageMinute(null));
        assertThrows(NullPointerException.class, () -> report.setMinuteFrameCut(null));
        assertThrows(NullPointerException.class, () -> report.setMetadata(null));
        assertThrows(NullPointerException.class, () -> report.setPercentStatusProcess(null));
        assertThrows(NullPointerException.class, () -> report.setReportTime(null));
        assertThrows(NullPointerException.class, () -> report.setStatus(null));
    }
}