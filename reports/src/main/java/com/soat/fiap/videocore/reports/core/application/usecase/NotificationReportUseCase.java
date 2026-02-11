package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.NotificationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por notificar clientes sobre atualizações de um reporte.
 */
@Component
@RequiredArgsConstructor
public class NotificationReportUseCase {

    private final NotificationGateway notificationGateway;

    /**
     * Envia a notificação do reporte para os clientes.
     *
     * @param report reporte atualizado
     */
    @WithSpan(name = "usecase.notificate.report")
    public void notificationReport(Report report) {

        if (report == null)
            throw new ReportException("O reporte não pode ser vazio para o disparo de notificações");

        notificationGateway.notificateReportClients(report);
    }
}