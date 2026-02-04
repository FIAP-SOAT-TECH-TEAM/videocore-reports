package com.soat.fiap.videocore.reports.infrastructure.in.websocket.config;

import com.soat.fiap.videocore.reports.infrastructure.common.websocket.WebSocketConstants;
import com.soat.fiap.videocore.reports.infrastructure.in.websocket.handlers.WebSocketErrorHandler;
import com.soat.fiap.videocore.reports.infrastructure.in.websocket.interceptors.channel.ReportTopicChannelInterceptor;
import com.soat.fiap.videocore.reports.infrastructure.in.websocket.interceptors.handshake.AuthSubjectHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuração do WebSocket com suporte a STOMP para comunicação em tempo real.
 *
 * <p>Expõe um endpoint para conexão WebSocket e habilita um broker simples para
 * publicação/assinatura de mensagens em tópicos.</p>
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthSubjectHandshakeInterceptor authSubjectHandshakeInterceptor;
    private final ReportTopicChannelInterceptor reportTopicChannelInterceptor;
    private final WebSocketErrorHandler webSocketErrorHandler;

    /**
     * Registra o endpoint STOMP utilizado pelos clientes para estabelecer a conexão WebSocket.
     *
     * @param registry registro de endpoints STOMP
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocketConstants.ENDPOINT_PREFIX)
                .addInterceptors(authSubjectHandshakeInterceptor);

        registry.setErrorHandler(webSocketErrorHandler);
    }

    /**
     * Registra interceptors no canal de entrada (inbound) para validar/autorizar mensagens STOMP dos clientes.
     *
     * @param registration registro do canal inbound
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(reportTopicChannelInterceptor);
    }

    /**
     * Configura o broker de mensagens para roteamento de destinos do tipo tópico.
     *
     * @param registry registro do broker de mensagens
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(WebSocketConstants.SUBSCRIBE_PREFIX_PATH_NAME);
    }
}