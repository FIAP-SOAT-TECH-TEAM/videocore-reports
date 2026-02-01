package com.soat.fiap.videocore.reports.infrastructure.out.event.azsvcbus.sender;

import com.azure.spring.cloud.service.servicebus.properties.ServiceBusEntityType;
import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ProcessVideoErrorEventDto;
import com.soat.fiap.videocore.reports.infrastructure.common.event.EventMessagingChannel;
import com.soat.fiap.videocore.reports.infrastructure.common.source.EventPublisherSource;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Implementação de {@link EventPublisherSource} para publicação de eventos no Azure Service Bus.
 */
@Component
@RequiredArgsConstructor
public class AzSvcEventPublisher implements EventPublisherSource {

    private final ServiceBusTemplate serviceBusTemplate;

    /**
     * Publica um evento de erro de processamento de vídeo na fila configurada.
     *
     * @param event Evento a ser publicado.
     */
    @Override
    public void publishProcessVideoErrorEvent(ProcessVideoErrorEventDto event) {
        var payload = MessageBuilder.withPayload(event).build();

        serviceBusTemplate.setDefaultEntityType(ServiceBusEntityType.QUEUE);
        serviceBusTemplate.send(EventMessagingChannel.PROCESS_ERROR_QUEUE, payload);
    }
}