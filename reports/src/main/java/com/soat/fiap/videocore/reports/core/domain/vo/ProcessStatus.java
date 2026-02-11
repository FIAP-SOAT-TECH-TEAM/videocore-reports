package com.soat.fiap.videocore.reports.core.domain.vo;

/**
 * Objeto de valor que representa o status do processamento de um vídeo.
 *
 * <p>Este enum descreve o ciclo de vida do processamento, desde a criação da solicitação
 * até a finalização.</p>
 */
public enum ProcessStatus {

    /**
     * Processamento iniciado.
     */
    STARTED,

    /**
     * Processamento em execução.
     */
    PROCESSING,

    /**
     * Processamento finalizado com sucesso.
     */
    COMPLETED,

    /**
     * Processamento com erro.
     */
    FAILED
}