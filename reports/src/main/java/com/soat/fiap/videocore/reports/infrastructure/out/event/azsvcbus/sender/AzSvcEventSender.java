package com.soat.fiap.videocore.reports.infrastructure.out.event.azsvcbus.sender;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ProcessVideoErrorEventDto;
import com.soat.fiap.videocore.reports.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.videocore.reports.infrastructure.common.exceptions.azure.svcbus.ServiceBusSerializationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementação de {@link EventPublisherSource} para publicação de eventos no Azure Service Bus.
 */
@Component
@RequiredArgsConstructor
public class AzSvcEventSender implements EventPublisherSource {

    private final ServiceBusSenderClient processErrorSender;
    private final ObjectMapper objectMapper;

    /**
     * Publica um evento de erro de processamento de vídeo na fila configurada.
     *
     * @param event Evento a ser publicado.
     */
    @Override
    public void publishProcessVideoErrorEvent(ProcessVideoErrorEventDto event)  {
        try {
            var rawEvent = objectMapper.writeValueAsString(event);
            var message = new ServiceBusMessage(rawEvent);

            processErrorSender.sendMessage(message);
        }
        catch (JsonProcessingException e) {
            throw new ServiceBusSerializationException("Erro ao serializar evento", e);
        }
    }
}