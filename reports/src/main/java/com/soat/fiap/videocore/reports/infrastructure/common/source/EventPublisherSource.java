package com.soat.fiap.videocore.reports.infrastructure.common.source;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ProcessVideoErrorEventDto;

/** Interface para publicação de eventos. */
public interface EventPublisherSource {

	/**
	 * Publica um evento indicando erro no processamento de um vídeo.
	 *
	 * @param event
	 *            DTO contendo informações do evento de erro no processamento do
	 *            vídeo
	 */
	void publishProcessVideoErrorEvent(ProcessVideoErrorEventDto event);
}
