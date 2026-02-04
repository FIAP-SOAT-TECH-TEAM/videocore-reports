package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.core.application.usecase.*;
import com.soat.fiap.videocore.reports.core.interfaceadapters.mapper.EventMapper;
import com.soat.fiap.videocore.reports.infrastructure.in.event.azsvcbus.payload.ProcessVideoStatusUpdatePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Controller responsável por orquestrar reportes de atualizações no status de processamento de um vídeo.
 */
@Component
@RequiredArgsConstructor
public class ProcessVideoStatusUpdateController {

    private final EventMapper reportMapper;
    private final CreateReportUseCase createReportUseCase;
    private final UpdateReportUseCase updateReportUseCase;
    private final GetExistingReportUseCase getExistingReportUseCase;
    private final SaveReportUseCase saveReportUseCase;
    private final NotificationReportUseCase notificationReportUseCase;

    /**
     * Processa a atualização no status de processamento de um vídeo através do payload de evento: {@link ProcessVideoStatusUpdatePayload}.
     *
     * @param entity Payload de atualização de status do vídeo.
     */
    public void processVideoStatusUpdate(ProcessVideoStatusUpdatePayload entity) {
        var input = reportMapper.toInput(entity);
        var newReport = createReportUseCase.createReport(input);

        var existingReport = getExistingReportUseCase.getExistingReport(input.userId(), input.requestId(), input.videoName(), input.percentStatusProcess());
        if (existingReport.isPresent()) {
            newReport = updateReportUseCase.updateReport(existingReport.get(), newReport);
        }

        var savedReport = saveReportUseCase.saveReport(newReport);
        notificationReportUseCase.notificationReport(savedReport);
    }
}