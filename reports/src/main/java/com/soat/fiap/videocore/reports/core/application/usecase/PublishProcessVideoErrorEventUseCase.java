package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.event.ProcessVideoErrorEvent;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.EventPublisherGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por publicar o evento de erro no processamento de vídeo.
 */
@Component
@RequiredArgsConstructor
public class PublishProcessVideoErrorEventUseCase {

    private final EventPublisherGateway eventPublisherGateway;

    /**
     * Publica um evento de erro de processamento a partir do reporte informado.
     *
     * @param report Reporte base para montagem do evento.
     */
    @WithSpan(name = "usecase.publish.process.video.error.event")
    public void publishProcessVideoErrorEvent(Report report) {

        if (report == null)
            throw new ReportException("O reporte não pode ser vazio para o disparo de eventos");

        var event = new ProcessVideoErrorEvent(
                report.getVideoName(),
                report.getUserId(),
                report.getRequestId(),
                report.getMinuteFrameCut(),
                report.getPercentStatusProcess(),
                report.getReportTime()
        );

        eventPublisherGateway.publishProcessVideoErrorEvent(event);
    }
}