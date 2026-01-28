package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.core.application.usecase.CreateReportUseCase;
import com.soat.fiap.videocore.reports.core.application.usecase.GetExistingReportUseCase;
import com.soat.fiap.videocore.reports.core.application.usecase.SaveReportUseCase;
import com.soat.fiap.videocore.reports.core.application.usecase.UpdateReportUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.mapper.EventMapper;
import com.soat.fiap.videocore.reports.infrastructure.in.event.listener.azsvcbus.payload.ProcessVideoStatusUpdatePayload;
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

        saveReportUseCase.saveReport(newReport);
    }
}