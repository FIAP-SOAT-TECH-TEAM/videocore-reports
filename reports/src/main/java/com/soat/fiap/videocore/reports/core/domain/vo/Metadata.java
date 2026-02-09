package com.soat.fiap.videocore.reports.core.domain.vo;

import java.util.Objects;

/**
 * Objeto de valor que representa metadados de rastreabilidade do processamento.
 * @param userId    Identificador do usuário associado ao processamento
 * @param requestId Identificador único da requisição
 * @param traceId   Identificador de rastreio (observabilidade).
 */
public record Metadata(String userId, String requestId, String traceId) {

    public Metadata {
        validate(userId, requestId);
    }

    private static void validate(String userId, String requestId) {
        Objects.requireNonNull(userId, "userId não pode ser nulo");
        Objects.requireNonNull(requestId, "requestId não pode ser nulo");
    }
}