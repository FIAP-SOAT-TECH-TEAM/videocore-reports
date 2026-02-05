package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.*;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Caso de uso responsável por processar falhas no processamento de vídeo,
 * atualizando ou criando um {@link Report} com status {@link ProcessStatus#FAILED}.
 */
@Component
@RequiredArgsConstructor
public class ProcessVideoErrorUseCase {

    private final ReportGateway reportGateway;
    private final PublishProcessVideoErrorEventUseCase publishProcessVideoErrorEventUseCase;

    /**
     * Processa um erro de vídeo, marcando o último relatório existente como FAILED
     * ou criando um novo relatório com status FAILED.
     *
     * @param videoDto dados do vídeo relacionados ao erro
     */
    @WithSpan(name = "usecase.process.video.error")
    public void processVideoError(VideoDto videoDto) {
        if (videoDto == null)
            throw new ReportException("O video não pode ser nulo para o processamento de erros");

        Report report;
        var now = Instant.now();

        var existingReport = reportGateway.getLastExistingReport(
                videoDto.userId(),
                videoDto.requestId(),
                videoDto.videoName()
        );

        if (existingReport.isPresent()) {
            report = existingReport.get();
            report.setStatus(ProcessStatus.FAILED);
        }
        else {
            var videoName = new VideoName(videoDto.videoName());
            var imageMinute = new ImageMinute(0L);
            var frameCute = new MinuteFrameCut(videoDto.minuteFrameCut());
            var metadata = new Metadata(videoDto.userId(), videoDto.requestId());
            var percentStatusProcess = new PercentStatusProcess(0.0);

            report = new Report(
                    videoName,
                    imageMinute,
                    frameCute,
                    metadata,
                    percentStatusProcess,
                    now,
                    ProcessStatus.FAILED
            );
        }

        reportGateway.save(report);

        publishProcessVideoErrorEventUseCase.publishProcessVideoErrorEvent(report, now);
    }
}