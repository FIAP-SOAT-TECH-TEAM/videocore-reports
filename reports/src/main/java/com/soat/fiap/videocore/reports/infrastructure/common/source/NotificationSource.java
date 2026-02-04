package com.soat.fiap.videocore.reports.infrastructure.common.source;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;

/**
 * Contrato para notificação de clientes sobre atualizações de reporte.
 */
public interface NotificationSource {

    /**
     * Notifica os clientes com o estado atual do reporte.
     *
     * @param reportDto dados do reporte
     */
    void notificateReportClients(ReportDto reportDto);
}