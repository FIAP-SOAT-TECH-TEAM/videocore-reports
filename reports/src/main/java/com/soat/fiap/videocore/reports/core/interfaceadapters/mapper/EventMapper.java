package com.soat.fiap.videocore.reports.core.interfaceadapters.mapper;

import org.mapstruct.Mapper;

import com.soat.fiap.videocore.reports.core.application.input.ReportInput;
import com.soat.fiap.videocore.reports.core.domain.event.ProcessVideoErrorEvent;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ProcessVideoErrorEventDto;
import com.soat.fiap.videocore.reports.infrastructure.in.event.azsvcbus.payload.ProcessVideoStatusUpdatePayload;

/**
 * Mapper responsável pela conversão entre payloads de eventos e outros objetos.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

	/**
	 * Converte um payload de atualização de status de vídeo em um objeto de input.
	 *
	 * @param payload
	 *            Payload recebido do evento.
	 * @return Objeto de input correspondente.
	 */
	ReportInput toInput(ProcessVideoStatusUpdatePayload payload);

	/**
	 * Converte um evento de erro de processamento de vídeo em um DTO.
	 *
	 * @param event
	 *            Evento de erro a ser convertido.
	 * @return DTO correspondente ao evento de erro.
	 */
	ProcessVideoErrorEventDto toDto(ProcessVideoErrorEvent event);
}
