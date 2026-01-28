package com.soat.fiap.videocore.reports.infrastructure.out.event.azsvcbus.sender;

import com.azure.spring.cloud.service.servicebus.properties.ServiceBusEntityType;
import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
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


}