package com.soat.fiap.videocore.reports.infrastructure.in.websocket.interceptors.channel;

import com.soat.fiap.videocore.reports.infrastructure.common.websocket.WebSocketConstants;
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
public class ReportTopicChannelInterceptor implements ChannelInterceptor {

    /**
     * Intercepta mensagens STOMP de {@link StompCommand#SUBSCRIBE} e bloqueia assinaturas
     * em destinos {@code /reports/{userId}} quando o {@code Auth-Subject} da sessão não corresponde.
     *
     * @param message mensagem STOMP
     * @param channel canal de envio
     * @return a própria mensagem quando permitida
     * @throws IllegalStateException quando {@code AuthSubject} está ausente ou não autorizado
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() != StompCommand.SUBSCRIBE) {
            return message;
        }

        var destination = accessor.getDestination();
        if (destination == null || !destination.startsWith(WebSocketConstants.REPORTS_FULL_PATH_NAME)) {
            return message;
        }

        var userIdInitialIndex = WebSocketConstants.REPORTS_FULL_PATH_NAME.length() + 1;
        var requestedUserId = destination.substring(userIdInitialIndex).trim();

        var authSubjectObj = accessor.getSessionAttributes() != null
                ? accessor.getSessionAttributes().get(WebSocketConstants.AUTH_SUBJECT_ATTR_NAME)
                : null;
        var authSubject = authSubjectObj != null ? authSubjectObj.toString() : null;

        if (authSubject == null || authSubject.isBlank()) {
            throw new IllegalStateException("AuthSubject ausente na sessão WebSocket");
        }
        if (!authSubject.equals(requestedUserId)) {
            throw new IllegalStateException("Acesso negado ao tópico: " + destination);
        }

        return message;
    }
}