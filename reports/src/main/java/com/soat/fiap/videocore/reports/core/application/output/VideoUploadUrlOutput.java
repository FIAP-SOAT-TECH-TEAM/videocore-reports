package com.soat.fiap.videocore.reports.core.application.output;

/**
 * Representa um DTO de saída da aplicação (Application Layer), contendo
 * informações sobre a URL de upload gerada.
 * @param url             Url de upload do vídeo
 * @param userId                Identificador do usuário dono do vídeo.
 * @param requestId             Identificador da requisição de processamento.
 */
public record VideoUploadUrlOutput(
        String url,
        String userId,
        String requestId
) {
}
