package com.soat.fiap.videocore.reports.core.interfaceadapters.gateway;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.TraceContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.event.ProcessVideoErrorEvent;
import com.soat.fiap.videocore.reports.core.interfaceadapters.mapper.EventMapper;
import com.soat.fiap.videocore.reports.infrastructure.common.source.EventPublisherSource;

import lombok.RequiredArgsConstructor;

/** Gateway responsável pela publicação de eventos de domínio. */
@Component @RequiredArgsConstructor
public class EventPublisherGateway {

	private final EventPublisherSource eventPublisherSource;
	private final EventMapper eventMapper;

	/**
	 * Publica um evento de erro no processamento do vídeo.
	 *
	 * @param event
	 *            evento de erro no processamento do vídeo
	 */
	@WithSpan(name = "gateway.publish.video.status.update.event")
	public void publishProcessVideoErrorEvent(ProcessVideoErrorEvent event) {
		var dto = eventMapper.toDto(event);

		eventPublisherSource.publishProcessVideoErrorEvent(dto);

		TraceContext.addEvent("event.object", event);
	}
}
