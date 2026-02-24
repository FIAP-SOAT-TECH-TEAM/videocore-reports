package com.soat.fiap.videocore.reports.infrastructure.common.websocket;

/** Constantes utilizadas na infraestrutura de WebSocket/STOMP. */
public class WebSocketConstants {

	/**
	 * Nome do Header passível de customizações durante um Handshake WebSocket.
	 * <a href=
	 * "https://stackoverflow.com/questions/4361173/http-headers-in-websockets-client-api">HTTP
	 * headers in Websockets client API</a>
	 */
	public static final String CUSTOM_HEADER_NAME = "Sec-WebSocket-Protocol";

	/** Nome do atributo de sessão que armazena o subject autenticado. */
	public static final String AUTH_SUBJECT_ATTR_NAME = "Auth-Subject";

	/** Prefixo de destino STOMP utilizado para assinatura. */
	public static final String SUBSCRIBE_PREFIX_PATH_NAME = "/topic";

	/**
	 * Prefixo do destino STOMP completo utilizado para publicação/assinatura de
	 * reportes.
	 */
	public static final String REPORTS_FULL_PATH_NAME = SUBSCRIBE_PREFIX_PATH_NAME;
}
