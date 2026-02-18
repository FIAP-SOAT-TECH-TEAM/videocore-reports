package com.soat.fiap.videocore.reports.core.interfaceadapters.gateway;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.mapper.ReportMapper;
import com.soat.fiap.videocore.reports.infrastructure.common.source.NotificationSource;

import lombok.RequiredArgsConstructor;

/**
 * Gateway responsável por abstrair o envio de notificações de reportes aos
 * clientes. Atua como intermediário entre a camada de aplicação e a fonte de
 * notificação.
 */
@Component @RequiredArgsConstructor
public class NotificationGateway {

	private final NotificationSource notificationSource;
	private final ReportMapper reportMapper;

	/**
	 * Notifica os clientes com o estado atual do reporte.
	 *
	 * @param report
	 *            dados do reporte
	 */
	@WithSpan(name = "gateway.notificate.report.clients")
	public void notificateReportClients(Report report) {
		var dto = reportMapper.toDto(report);
		notificationSource.notificateReportClients(dto);
	}
}
