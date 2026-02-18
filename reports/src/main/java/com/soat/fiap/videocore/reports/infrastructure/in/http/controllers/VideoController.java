package com.soat.fiap.videocore.reports.infrastructure.in.http.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserVideoImagesDownloadUrlController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserVideoUploadUrlController;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoImagesDownloadUrlResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoUploadUrlResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** Controlador REST para gerenciamento de videos */
@RestController @RequestMapping("/video") @Tag(name = "Video") @RequiredArgsConstructor @Slf4j
public class VideoController {

	private final GetAuthUserVideoImagesDownloadUrlController getAuthUserVideoImagesDownloadUrlController;
	private final GetAuthUserVideoUploadUrlController getAuthUserVideoUploadUrlController;

	@GetMapping("/download/url")
	@Operation(summary = "Obter URL de download das imagens de um vídeo enviado pelo usuário autenticado", description = "Retorna a URL de download das imagens capturadas de um vídeo enviado pelo usuário autenticado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "URL de download gerada com sucesso", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = VideoImagesDownloadUrlResponse.class)))),
			@ApiResponse(responseCode = "400", description = "Quando o ID da requisição ou o nome do vídeo forem informados incorretamente", content = @Content),
			@ApiResponse(responseCode = "404", description = "Quando a URL de download do arquivo de imagens do vídeo informado não for encontrada", content = @Content),
			@ApiResponse(responseCode = "401", description = "Quando não for possível obter o ID do usuário autenticado (Header HTTTP Auth-Subject)", content = @Content)})
	public ResponseEntity<VideoImagesDownloadUrlResponse> getAuthUserVideoImagesDownloadUrl(
			@Parameter(description = "Identificador único da requisição", example = "req-9f8c7a12-1234-4cde-a321-abcdef123456")
			@RequestParam String requestId,
			@Parameter(description = "Nome do vídeo enviado pelo usuário", example = "video-aula-matematica.mp4")
			@RequestParam String videoName) {
		try {
			var downloadUrl = getAuthUserVideoImagesDownloadUrlController.getVideoImagesDownloadUrl(requestId,
					videoName);

			log.info("request_completed");

			return ResponseEntity.ok(downloadUrl);
		} catch (Exception e) {
			log.error("request_error");

			throw e;
		} finally {
			CanonicalContext.clear();
		}
	}

	@GetMapping("/upload/url")
	@Operation(summary = "Obter URLs de upload a partir de uma lista de vídeos para processamento, para o usuário autenticado", description = "Retorna URLs de uploads de vídeos para processamento")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "URL de upload gerada com sucesso", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VideoUploadUrlResponse.class))),
			@ApiResponse(responseCode = "400", description = """
					Erro de validação:
					- Lista de nomes vazia
					- Nome inválido na lista
					- Mais de 3 vídeos enviados
					- Nomes duplicados
					""", content = @Content),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)})
	public ResponseEntity<List<VideoUploadUrlResponse>> getAuthUserVideoUploadUrl(
			@Parameter(description = "Lista de nomes dos vídeos para upload (máximo 3)", example = "video1.mp4,video2.mp4")
			@RequestParam List<String> videoNames) {
		try {
			var uploadUrl = getAuthUserVideoUploadUrlController.getVideoUploadUrl(videoNames);

			log.info("request_completed");

			return ResponseEntity.ok(uploadUrl);
		} catch (Exception e) {
			log.error("request_error");

			throw e;
		} finally {
			CanonicalContext.clear();
		}
	}
}
