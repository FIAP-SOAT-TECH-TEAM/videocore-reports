package com.soat.fiap.videocore.reports.unit.controller;

import com.soat.fiap.videocore.reports.core.application.input.ReportInput;
import com.soat.fiap.videocore.reports.core.application.usecase.*;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.ProcessVideoStatusUpdateController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.mapper.EventMapper;
import com.soat.fiap.videocore.reports.infrastructure.in.event.azsvcbus.payload.ProcessVideoStatusUpdatePayload;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link ProcessVideoStatusUpdateController}.
 */
class ProcessVideoStatusUpdateControllerTest {

    @Test
    void shouldCreateAndSaveReportWhenNoExistingReport() {
        // Arrange
        EventMapper mapper = mock(EventMapper.class);
        CreateReportUseCase create = mock(CreateReportUseCase.class);
        UpdateReportUseCase update = mock(UpdateReportUseCase.class);
        GetExistingReportUseCase getExisting = mock(GetExistingReportUseCase.class);
        SaveReportUseCase save = mock(SaveReportUseCase.class);
        NotificationReportUseCase notify = mock(NotificationReportUseCase.class);
        PublishProcessVideoErrorEventUseCase publishError = mock(PublishProcessVideoErrorEventUseCase.class);

        var payload = mock(ProcessVideoStatusUpdatePayload.class);
        var input = mock(ReportInput.class);
        var report = mock(Report.class);

        when(mapper.toInput(payload)).thenReturn(input);
        when(create.createReport(input)).thenReturn(report);
        when(getExisting.getExistingReport(any(), any(), any(), any()))
                .thenReturn(Optional.empty());
        when(save.saveReport(report)).thenReturn(report);
        when(report.getStatus()).thenReturn(ProcessStatus.STARTED);

        var controller = new ProcessVideoStatusUpdateController(
                mapper, create, update, getExisting, save, notify, publishError
        );

        // Act
        controller.processVideoStatusUpdate(payload);

        // Assert
        verify(create).createReport(input);
        verify(update, never()).updateReport(any(), any());
        verify(save).saveReport(report);
        verify(notify).notificationReport(report);
        verify(publishError, never()).publishProcessVideoErrorEvent(any());
    }

    @Test
    void shouldUpdateAndPublishErrorWhenReportFails() {
        // Arrange
        EventMapper mapper = mock(EventMapper.class);
        CreateReportUseCase create = mock(CreateReportUseCase.class);
        UpdateReportUseCase update = mock(UpdateReportUseCase.class);
        GetExistingReportUseCase getExisting = mock(GetExistingReportUseCase.class);
        SaveReportUseCase save = mock(SaveReportUseCase.class);
        NotificationReportUseCase notify = mock(NotificationReportUseCase.class);
        PublishProcessVideoErrorEventUseCase publishError = mock(PublishProcessVideoErrorEventUseCase.class);

        var payload = mock(ProcessVideoStatusUpdatePayload.class);
        var input = mock(ReportInput.class);
        var newReport = mock(Report.class);
        var existingReport = mock(Report.class);
        var updatedReport = mock(Report.class);

        when(mapper.toInput(payload)).thenReturn(input);
        when(create.createReport(input)).thenReturn(newReport);
        when(getExisting.getExistingReport(any(), any(), any(), any()))
                .thenReturn(Optional.of(existingReport));
        when(update.updateReport(existingReport, newReport)).thenReturn(updatedReport);
        when(save.saveReport(updatedReport)).thenReturn(updatedReport);
        when(updatedReport.getStatus()).thenReturn(ProcessStatus.FAILED);

        var controller = new ProcessVideoStatusUpdateController(
                mapper, create, update, getExisting, save, notify, publishError
        );

        // Act
        controller.processVideoStatusUpdate(payload);

        // Assert
        verify(update).updateReport(existingReport, newReport);
        verify(save).saveReport(updatedReport);
        verify(notify).notificationReport(updatedReport);
        verify(publishError).publishProcessVideoErrorEvent(updatedReport);
    }
}