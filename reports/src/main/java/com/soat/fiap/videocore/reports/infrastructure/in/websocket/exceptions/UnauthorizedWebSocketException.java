package com.soat.fiap.videocore.reports.infrastructure.in.websocket.exceptions;

/**
 * Exceção lançada quando um erro de autenticação ocorre no contexto de uma
 * requisição WebSocket
 */
public class UnauthorizedWebSocketException extends RuntimeException {

	public UnauthorizedWebSocketException(String message) {
		super(message);
	}

	public UnauthorizedWebSocketException(String message, Throwable cause) {
		super(message, cause);
	}
}
