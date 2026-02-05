package com.soat.fiap.videocore.reports.infrastructure.in.http.controllers;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetVideoImagesDownloadUrlController;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoImagesDownloadUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para gerenciamento de videos
 */
@RestController
@RequestMapping("/video")
@Tag(name = "Video")
@RequiredArgsConstructor
@Slf4j
public class VideoController {

    private final GetVideoImagesDownloadUrlController getVideoImagesDownloadUrlController;

    @GetMapping("/image/url")
    @Operation(
            summary = "Buscar URL de download das imagens de um vídeo",
            description = "Retorna a URL de download das imagens capturadas de um vídeo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "URL de download gerada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = VideoImagesDownloadUrlResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quando o ID do usuário, da requisição o nome do vídeo forem informados incorretamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quando a URL de download do arquivo de imagens do vídeo informado não for encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<VideoImagesDownloadUrlResponse> getVideoImagesDownloadUrl(@RequestParam String userId, @RequestParam String requestId, @RequestParam String videoName
    ) {
        try {
            var downloadUrl = getVideoImagesDownloadUrlController.getVideoImagesDownloadUrl(userId, requestId, videoName);

            log.info("request_completed");

            return ResponseEntity.ok(downloadUrl);
        }
        finally {
            CanonicalContext.clear();
        }
    }
}
