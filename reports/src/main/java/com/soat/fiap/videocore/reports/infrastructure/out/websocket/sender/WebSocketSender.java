package com.soat.fiap.videocore.reports.infrastructure.out.websocket.sender;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.common.source.NotificationSource;
import com.soat.fiap.videocore.reports.infrastructure.common.websocket.WebSocketConstants;
import com.soat.fiap.videocore.reports.infrastructure.in.websocket.mapper.WebSocketMapper;

import lombok.RequiredArgsConstructor;

/**
 * Sender responsável por publicar atualizações de reportes via WebSocket
 * (STOMP).
 */
@Component @RequiredArgsConstructor
public class WebSocketSender implements NotificationSource {

	private final SimpMessagingTemplate messagingTemplate;
	private final WebSocketMapper webSocketMapper;

	/**
	 * Publica o reporte no tópico do usuário para consumo pelos clientes.
	 *
	 * @param reportDto
	 *            dados do reporte
	 */
	@Override
	public void notificateReportClients(ReportDto reportDto) {
		var payload = webSocketMapper.toPayload(reportDto);
		var path = String.format("%s/%s", WebSocketConstants.REPORTS_FULL_PATH_NAME, payload.userId());

		messagingTemplate.convertAndSend(path, payload);
	}
}
