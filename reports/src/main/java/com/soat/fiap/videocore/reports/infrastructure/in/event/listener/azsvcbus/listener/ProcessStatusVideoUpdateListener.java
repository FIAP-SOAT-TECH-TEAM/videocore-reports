package com.soat.fiap.videocore.reports.infrastructure.in.event.listener.azsvcbus.listener;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.ProcessVideoStatusUpdateController;
import com.soat.fiap.videocore.reports.infrastructure.common.event.EventMessagingChannel;
import com.soat.fiap.videocore.reports.infrastructure.in.event.listener.azsvcbus.payload.ProcessVideoStatusUpdatePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Listener responsável por consumir eventos da fila de atualização do status de processamento de um vídeo.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessStatusVideoUpdateListener {

    private final ObjectMapper objectMapper;
    private final ProcessVideoStatusUpdateController processVideoStatusUpdateController;

    /**
     * Consome mensagens da fila {@link EventMessagingChannel#PROCESS_STATUS_TOPIC}.
     *
     * @param message Wrapper da mensagem recebida
     */
    @WithSpan(name = "process.report.event")
    @ServiceBusListener(destination = EventMessagingChannel.PROCESS_STATUS_TOPIC,
            group = EventMessagingChannel.REPORTS_PROCESS_STATUS_TOPIC_SUBSCRIPTION)
    public void processEvent(ServiceBusReceivedMessage message) throws Exception {
        try {
            var rawBody = message.getBody().toString();
            CanonicalContext.add("event_object_string", rawBody);

            var body = objectMapper.readValue(rawBody, ProcessVideoStatusUpdatePayload.class);
            CanonicalContext.add("event_object", body);

            processVideoStatusUpdateController.processVideoStatusUpdate(body);

            log.info("request_completed");
        } finally {
            CanonicalContext.clear();
        }
    }
}