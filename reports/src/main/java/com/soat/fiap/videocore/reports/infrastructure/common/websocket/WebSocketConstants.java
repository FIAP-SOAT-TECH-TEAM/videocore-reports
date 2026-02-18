package com.soat.fiap.videocore.reports.infrastructure.common.websocket;

/** Constantes utilizadas na infraestrutura de WebSocket/STOMP. */
public class WebSocketConstants {

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
