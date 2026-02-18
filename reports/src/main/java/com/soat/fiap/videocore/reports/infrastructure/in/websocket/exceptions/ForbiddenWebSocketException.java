package com.soat.fiap.videocore.reports.infrastructure.in.websocket.exceptions;

/**
 * Exceção lançada quando um erro de autorização ocorre no contexto de uma
 * requisição WebSocket
 */
public class ForbiddenWebSocketException extends RuntimeException {

	public ForbiddenWebSocketException(String message) {
		super(message);
	}

	public ForbiddenWebSocketException(String message, Throwable cause) {
		super(message, cause);
	}
}
