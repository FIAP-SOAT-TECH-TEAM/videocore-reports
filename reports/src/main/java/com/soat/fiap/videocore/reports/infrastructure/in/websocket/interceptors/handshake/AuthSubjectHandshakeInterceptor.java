package com.soat.fiap.videocore.reports.infrastructure.in.websocket.interceptors.handshake;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.infrastructure.common.websocket.WebSocketConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Interceptor de handshake WebSocket responsável por validar e propagar o header {@code Auth-Subject}
 * para os atributos da sessão.
 */
@Component
@Slf4j
public class AuthSubjectHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * Valida a presença do header {@code Auth-Subject} e o armazena nos atributos do handshake.
     *
     * @param request    requisição HTTP do handshake
     * @param response   resposta HTTP do handshake
     * @param wsHandler  handler WebSocket associado
     * @param attributes mapa de atributos da sessão WebSocket
     * @return {@code true} se o header estiver presente e válido; caso contrário {@code false}
     */
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        try {
            var values = request.getHeaders().get(WebSocketConstants.AUTH_SUBJECT_ATTR_NAME);

            CanonicalContext.add("remote_address", request.getRemoteAddress().toString());

            if (values == null || values.isEmpty() || values.getFirst().isBlank()) {
                var statusCode = HttpStatusCode.valueOf(401);
                response.setStatusCode(statusCode);

                CanonicalContext.add("event", "WEBSOCKET_HANDSHAKE_REJECTED");
                CanonicalContext.add("auth_subject", "");
                CanonicalContext.add("response_status_code", statusCode);

                log.warn("request_completed");

                return false;
            }

            var subject = values.getFirst();
            attributes.put(WebSocketConstants.AUTH_SUBJECT_ATTR_NAME, subject);

            CanonicalContext.add("event", "WEBSOCKET_HANDSHAKE_ACCEPTED");
            CanonicalContext.add("auth_subject", subject);

            log.info("request_completed");

            return true;
        }
        finally {
            CanonicalContext.clear();
        }
    }

    /**
     * Hook executado após o handshake.
     *
     * @param request   requisição HTTP do handshake
     * @param response  resposta HTTP do handshake
     * @param wsHandler handler WebSocket associado
     * @param exception exceção ocorrida durante o handshake, se houver
     */
    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        // Not implemented
    }
}