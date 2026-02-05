package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ProcessReportException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por salvar um reporte de atualização do status de processamento de um vídeo.
 */
@Component
@RequiredArgsConstructor
public class SaveReportUseCase {
    private final ReportGateway reportGateway;

    /**
     * Salva um reporte.
     *
     * @param report reporte a ser salvo.
     * @return Reporte salvo.
     * @throws ProcessReportException Se ocorrer erro ao salvar o reporte.
     */
    @WithSpan(name = "usecase.save.report")
    public Report saveReport(Report report) {
        try {
            if (report == null)
                throw new ReportException("O reporte a ser persistido não pode ser nulo");

            return reportGateway.save(report);
        }
        catch (Exception e) {
            throw new ProcessReportException(String.format("Erro no processamento do reporte de atualização do status de processamento de um vídeo: %s", e.getMessage()), e);
        }
    }
}
