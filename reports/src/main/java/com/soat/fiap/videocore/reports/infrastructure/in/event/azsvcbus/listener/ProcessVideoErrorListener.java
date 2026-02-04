package com.soat.fiap.videocore.reports.infrastructure.in.event.azsvcbus.listener;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.ProcessVideoErrorController;
import com.soat.fiap.videocore.reports.infrastructure.common.event.EventMessagingChannel;
import com.soat.fiap.videocore.reports.infrastructure.in.event.azsvcbus.payload.BlobCreatedCloudEventSchemaPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Listener responsável por consumir mensagens da DLQ e acionar o fluxo de tratamento de erro.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessVideoErrorListener {

    private final ObjectMapper objectMapper;
    private final ProcessVideoErrorController processVideoErrorController;

    /**
     * Processa a mensagem recebida da DLQ, extrai a URL do vídeo e delega o tratamento ao controller.
     *
     * @param message Mensagem recebida do Azure Service Bus.
     * @throws Exception Em caso de falha ao desserializar ou processar o evento.
     */
    @WithSpan(name = "process.video.error")
    @ServiceBusListener(destination = EventMessagingChannel.PROCESS_QUEUE_DLQ)
    public void processEvent(ServiceBusReceivedMessage message) throws Exception {
        try {
            var rawBody = message.getBody().toString();
            CanonicalContext.add("event_object_string", rawBody);

            var body = objectMapper.readValue(rawBody, BlobCreatedCloudEventSchemaPayload.class);
            CanonicalContext.add("event_object", body);

            processVideoErrorController.processVideoError(body.getData().getUrl());

            log.info("request_completed");
        } finally {
            CanonicalContext.clear();
        }
    }
}