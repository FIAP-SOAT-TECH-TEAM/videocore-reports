package com.soat.fiap.videocore.reports.infrastructure.in.websocket.interceptors.channel;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.infrastructure.common.websocket.WebSocketConstants;
import com.soat.fiap.videocore.reports.infrastructure.in.websocket.exceptions.UnauthorizedWebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 * Interceptor de canal STOMP que valida assinaturas em tópicos de reportes,
 * garantindo que o usuário autenticado só assine o próprio tópico.
 */
@Component
@Slf4j
public class ReportTopicChannelInterceptor implements ChannelInterceptor {

    /**
     * Intercepta mensagens STOMP de {@link StompCommand#SUBSCRIBE}, bloqueia assinaturas
     * quando o {@code Auth-Subject} da sessão não existe, e normaliza o destino da assinatura
     * de acordo com o usuário autenticado.
     *
     * @param message mensagem STOMP
     * @param channel canal de envio
     * @return a própria mensagem quando permitida
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);

        try {
            if (accessor.getCommand() != StompCommand.SUBSCRIBE) {
                return message;
            }

            CanonicalContext.add("event", "WEBSOCKET_SUBSCRIBE_ATTEMPT");
            CanonicalContext.add("stomp_command", accessor.getCommand().name());
            CanonicalContext.add("destination", accessor.getDestination());

            var destination = accessor.getDestination();
            if (destination == null || !destination.startsWith(WebSocketConstants.REPORTS_FULL_PATH_NAME)) {
                CanonicalContext.add("event", "WEBSOCKET_SUBSCRIBE_IGNORED");
                log.info("request_completed");
                return message;
            }

            var authSubjectObj = accessor.getSessionAttributes() != null
                    ? accessor.getSessionAttributes().get(WebSocketConstants.AUTH_SUBJECT_ATTR_NAME)
                    : null;
            var authSubject = authSubjectObj != null ? authSubjectObj.toString() : null;

            if (authSubject == null || authSubject.isBlank()) {
                CanonicalContext.add("event", "WEBSOCKET_SUBSCRIBE_REJECTED");
                CanonicalContext.add("auth_subject", "");

                log.warn("request_completed");
                throw new UnauthorizedWebSocketException("AuthSubject ausente na sessão WebSocket");
            }

            var normalizedDestination =
                    WebSocketConstants.REPORTS_FULL_PATH_NAME + "/" + authSubject;

            accessor.setDestination(normalizedDestination);

            CanonicalContext.add("event", "WEBSOCKET_SUBSCRIBE_ACCEPTED");
            CanonicalContext.add("auth_subject", authSubject);
            CanonicalContext.add("normalized_destination", normalizedDestination);

            log.info("request_completed");

            return message;
        }
        finally {
            CanonicalContext.clear();
        }
    }
}