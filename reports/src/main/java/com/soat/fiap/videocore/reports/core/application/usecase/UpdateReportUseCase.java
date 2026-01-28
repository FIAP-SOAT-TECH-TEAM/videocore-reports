package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.DurationMinutes;
import com.soat.fiap.videocore.reports.core.domain.vo.MinuteFrameCut;
import com.soat.fiap.videocore.reports.core.domain.vo.PercentStatusProcess;
import com.soat.fiap.videocore.reports.core.domain.vo.VideoName;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por atualizar um {@link Report} existente com base em um novo {@link Report}.
 *
 * <p>Este caso de uso aplica os valores do {@code newReport} no {@code report} utilizando os setters
 * do {@code report} e retorna a mesma instância atualizada ao final.</p>
 */
@Component
public class UpdateReportUseCase {

    /**
     * Atualiza um reporte existente com os dados de um novo reporte.
     *
     * <p>Este método não cria uma nova instância de {@link Report}. Ele modifica o objeto recebido em {@code report}
     * aplicando os valores do {@code newReport} através de setters.</p>
     *
     * @param report reporte existente que será atualizado
     * @param newReport reporte contendo os novos valores que devem ser aplicados
     * @return o mesmo {@code report} atualizado com os dados do {@code newReport}
     */
    @WithSpan(name = "process.report.update-report")
    public Report updateReport(Report report, Report newReport) {

        var videoName = new VideoName(newReport.getVideoName());
        var durationMinutes = new DurationMinutes(newReport.getDurationMinutes());
        var minuteFrameCut = new MinuteFrameCut(newReport.getMinuteFrameCut());
        var percentStatus = new PercentStatusProcess(newReport.getPercentStatusProcess());

        report.setVideoName(videoName);
        report.setDurationMinutes(durationMinutes);
        report.setMinuteFrameCut(minuteFrameCut);
        report.setMetadata(newReport.getMetadata());
        report.setPercentStatusProcess(percentStatus);
        report.setReportTime(newReport.getReportTime());
        report.setStatus(newReport.getStatus());

        return report;
    }
}