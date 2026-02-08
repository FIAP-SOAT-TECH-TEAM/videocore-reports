package com.soat.fiap.videocore.reports.infrastructure.in.http.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidade de resposta para solicitações de URL de upload
 * de um vídeo via API HTTP.
 */
@Schema(
        name = "VideoUploadUrlResponse",
        description = "Representa a URL para upload de um vídeo para processamento"
)
public record VideoUploadUrlResponse(

        @Schema(
                description = "URL para upload do vídeo",
                example = "https://storage.exemplo.com/videos/123/456/video.mp4?token=1707055200"
        )
        String url
) {
}