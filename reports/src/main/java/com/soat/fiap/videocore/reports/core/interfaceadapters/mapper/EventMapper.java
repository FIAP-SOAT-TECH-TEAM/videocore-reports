package com.soat.fiap.videocore.reports.core.interfaceadapters.mapper;

import com.soat.fiap.videocore.reports.core.application.input.ReportInput;
import com.soat.fiap.videocore.reports.infrastructure.in.event.listener.azsvcbus.payload.ProcessVideoStatusUpdatePayload;
import org.mapstruct.Mapper;

/**
 * Mapper responsável pela conversão entre payloads de eventos e outros objetos.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

    /**
     * Converte um payload de atualização de status de vídeo em um objeto de input.
     *
     * @param payload Payload recebido do evento.
     * @return Objeto de input correspondente.
     */
    ReportInput toInput(ProcessVideoStatusUpdatePayload payload);

}