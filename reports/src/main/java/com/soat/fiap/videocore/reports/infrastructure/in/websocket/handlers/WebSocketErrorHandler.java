package com.soat.fiap.videocore.reports.infrastructure.in.websocket.handlers;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import com.soat.fiap.videocore.reports.infrastructure.in.websocket.exceptions.ForbiddenWebSocketException;
import com.soat.fiap.videocore.reports.infrastructure.in.websocket.exceptions.UnauthorizedWebSocketException;

/**
 * Handler de erros do protocolo STOMP para padronizar respostas de falha ao
 * cliente.
 *
 * <p>
 * Converte exceções em frames {@code ERROR} e adiciona um {@code error-code}
 * para casos conhecidos.
 */
@Component
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

	/**
	 * Trata erros durante o processamento de mensagens do cliente e devolve um
	 * frame {@code ERROR}.
	 *
	 * @param clientMessage
	 *            mensagem original enviada pelo cliente
	 * @param exception
	 *            exceção lançada durante o processamento
	 * @return mensagem STOMP {@code ERROR} com payload e headers apropriados
	 */
	@Override
	public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable exception) {
		var innerEx = exception.getCause();
		var accessor = StompHeaderAccessor.create(StompCommand.ERROR);

		accessor.setMessage(resolveClientMessage(innerEx));

		if (innerEx instanceof UnauthorizedWebSocketException) {
			accessor.addNativeHeader("error-code", "SUBSCRIBE_UNAUTHORIZED");
		} else if (innerEx instanceof ForbiddenWebSocketException) {
			accessor.addNativeHeader("error-code", "SUBSCRIBE_FORBIDDEN");
		}

		var payload = resolveClientMessage(innerEx).getBytes();

		return MessageBuilder.createMessage(payload, accessor.getMessageHeaders());
	}

	/**
	 * Resolve uma mensagem segura para retorno ao cliente.
	 *
	 * @param ex
	 *            exceção capturada
	 * @return mensagem de erro não vazia
	 */
	private String resolveClientMessage(Throwable ex) {
		var msg = ex.getMessage();
		if (msg == null || msg.isBlank()) {
			return "Erro ao processar mensagem STOMP";
		}

		return msg;
	}
}
