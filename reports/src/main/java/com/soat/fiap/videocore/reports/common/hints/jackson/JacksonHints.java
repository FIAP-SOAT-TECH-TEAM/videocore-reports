package com.soat.fiap.videocore.reports.common.hints.jackson;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ProcessVideoErrorEventDto;
import com.soat.fiap.videocore.reports.infrastructure.in.event.azsvcbus.payload.ProcessVideoStatusUpdatePayload;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * RuntimeHints para suporte a serialização e desserialização com Jackson
 * em ambiente GraalVM Native Image.
 *
 * <p>
 * Registra reflexão para:
 * <ul>
 *     <li>Payloads desserializados via ObjectMapper</li>
 *     <li>DTOs serializados para envio ao Service Bus</li>
 *     <li>Tipos do Azure Service Bus acessados via reflexão</li>
 * </ul>
 *
 * <p>
 * Necessário pois o Jackson utiliza reflexão para:
 * <ul>
 *     <li>Instanciar classes</li>
 *     <li>Acessar campos</li>
 *     <li>Invocar getters/setters</li>
 * </ul>
 * Em Native Image, esses acessos precisam ser explicitamente registrados.
 */
public class JacksonHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        registerJacksonType(hints, ProcessVideoStatusUpdatePayload.class);
        registerJacksonType(hints, ProcessVideoErrorEventDto.class);
        registerJacksonType(hints, ServiceBusMessage.class);
        registerJacksonType(hints, ServiceBusReceivedMessageContext.class);
        registerJacksonType(hints, ObjectMapper.class);
    }

    private void registerJacksonType(RuntimeHints hints, Class<?> type) {
        hints.reflection().registerType(
                type,
                MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_PUBLIC_METHODS,
                MemberCategory.INVOKE_DECLARED_METHODS,
                MemberCategory.ACCESS_DECLARED_FIELDS
        );
    }
}