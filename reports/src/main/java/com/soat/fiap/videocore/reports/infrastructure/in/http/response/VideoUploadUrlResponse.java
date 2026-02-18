package com.soat.fiap.videocore.reports.infrastructure.in.http.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidade de resposta para solicitações de URL de upload de um vídeo via API
 * HTTP.
 */
@Schema(name = "VideoUploadUrlResponse", description = "Representa a URL para upload de um vídeo para processamento")
public record VideoUploadUrlResponse(
		@Schema(description = "URL para upload do vídeo", example = "https://storage.exemplo.com/videos/123/456/video.mp4?token=1707055200") String url,
		@Schema(description = "Identificador do usuário dono do vídeo", example = "3f1e2c4a-8b7d-4e5f-9a1b-2c3d4e5f6a7b") String userId,
		@Schema(description = "Identificador da requisição de processamento", example = "9a8b7c6d-5e4f-3a2b-1c0d-9e8f7a6b5c4d") String requestId) {
}
