package com.soat.fiap.videocore.reports.infrastructure.common.event;

/**
 * Centraliza os nomes dos canais de mensageria (filas, tópicos e subscriptions).
 */
public final class EventMessagingChannel {

    private EventMessagingChannel() {
    }

    /**
     * Fila principal de processamento de eventos.
     * <p>Responsável pelo fluxo normal de processamento.</p>
     */
    public static final String PROCESS_QUEUE = "process.queue";

    /**
     * Fila principal de erro no processamento do vídeo.
     * <p>Complemento a DLQ da fila de processamento do vídeo.</p>
     */
    public static final String PROCESS_ERROR_QUEUE = "process.error.queue";

    /**
     * Tópico de status do processamento.
     * <p>Publica atualizações de estado dos processos.</p>
     */
    public static final String PROCESS_STATUS_TOPIC = "process.status.topic";

    /**
     * Assinatura do Tópico de status do processamento.
     * <p>Microsserviço de reports.</p>
     */
    public static final String REPORTS_PROCESS_STATUS_TOPIC_SUBSCRIPTION = "reports.process.status.topic.subscription";


}