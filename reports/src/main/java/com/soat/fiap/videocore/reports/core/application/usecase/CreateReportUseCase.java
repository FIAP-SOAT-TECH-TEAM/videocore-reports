package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.input.ReportInput;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.*;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por criar um reporte a partir dos dados de entrada.
 */
@Component
public class CreateReportUseCase {

    /**
     * Cria um reporte com base nas informações fornecidas
     *
     * <p>Identificador único é nulo neste momento.</p>
     * @param reportInput dados de entrada do reporte
     * @return reporte criado
     */
    @WithSpan(name = "process.report.create-report")
    public Report createReport(ReportInput reportInput) {
        var videoName = new VideoName(reportInput.videoName());
        var durationMinutes = new DurationMinutes(reportInput.durationMinutes());
        var minuteFrameCut = new MinuteFrameCut(reportInput.frameCutMinutes());
        var metadata = new Metadata(reportInput.userId(), reportInput.requestId());
        var percentStatusProcess = new PercentStatusProcess(reportInput.percentStatusProcess());

        var processStatus = ProcessStatus.STARTED;

        if (percentStatusProcess.value() > 0.0 && percentStatusProcess.value() < 100.0) {
            processStatus = ProcessStatus.PROCESSING;
        }
        else if (percentStatusProcess.value() == 100.00) {
            processStatus = ProcessStatus.COMPLETED;
        }

        return new Report(
                videoName,
                durationMinutes,
                minuteFrameCut,
                metadata,
                percentStatusProcess,
                reportInput.reportTime(),
                processStatus
        );
    }
}