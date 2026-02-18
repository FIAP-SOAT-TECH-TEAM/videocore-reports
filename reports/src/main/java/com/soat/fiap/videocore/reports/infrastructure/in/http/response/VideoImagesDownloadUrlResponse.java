package com.soat.fiap.videocore.reports.infrastructure.in.http.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidade de resposta para solicitações de URL de download das imagens
 * capturadas de um vídeo via API HTTP.
 */
@Schema(name = "VideoImagesDownloadUrlResponse", description = "Representa a URL para download das imagens capturadas de um vídeo")
public record VideoImagesDownloadUrlResponse(
		@Schema(description = "URL para download das imagens do vídeo", example = "https://storage.exemplo.com/images/123/123/images.zip?token=1707055200") String url) {
}
