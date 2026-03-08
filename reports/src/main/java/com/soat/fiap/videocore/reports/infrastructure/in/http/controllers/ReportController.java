package com.soat.fiap.videocore.reports.infrastructure.in.http.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserLastExistingReportController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserLastReportsController;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.PaginationResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.swagger.ReportPaginationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** Controlador REST para gerenciamento de reportes */
@RestController @RequestMapping("/") @Tag(name = "Report") @RequiredArgsConstructor @Slf4j
public class ReportController {

	private final GetAuthUserLastReportsController getAuthUserLastReportsController;
	private final GetAuthUserLastExistingReportController getAuthUserLastExistingReportController;

	@GetMapping("/latest")
	@Operation(summary = "Obter os reportes mais recentes do usuário autenticado", description = "Retorna a lista paginada de reportes mais recentes dos vídeos enviados pelo usuário autenticado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reportes encontrados", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportPaginationResponse.class))),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Quando não for possível obter o ID do usuário autenticado (Header HTTP Auth-Subject)", content = @Content)})
	public ResponseEntity<PaginationResponse<ReportResponse>> getAuthUserLastReports(
			@Parameter(description = "Número da página (baseado em zero)", example = "0")
			@RequestParam(defaultValue = "0") int page,

			@Parameter(description = "Quantidade de elementos por página", example = "10")
			@RequestParam(defaultValue = "10") int size) {
		try {
			var reports = getAuthUserLastReportsController.getAuthenticatedUserLastReports(page, size);

			log.info("request_completed");

			return ResponseEntity.ok(reports);
		} catch (Exception e) {
			log.error("request_error", e);
			throw e;
		} finally {
			CanonicalContext.clear();
		}
	}

	@GetMapping
	@Operation(summary = "Obter último reporte existente de um vídeo enviado pelo usuário autenticado", description = "Retorna o último reporte existente de um vídeo enviado pelo usuário autenticado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reporte encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportResponse.class))),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Reporte não encontrado", content = @Content),
			@ApiResponse(responseCode = "401", description = "Quando não for possível obter o ID do usuário autenticado (Header HTTP Auth-Subject)", content = @Content),
			@ApiResponse(responseCode = "403", description = "Quando o reporte não pertencer ao usuário autenticado", content = @Content)})
	public ResponseEntity<ReportResponse> getLastExistingReport(@RequestParam
	@Schema(description = "Identificador da requisição", example = "3c29043a-f5b0-482b-ad20-ff6c7310d9ee", requiredMode = Schema.RequiredMode.REQUIRED) String requestId,

			@RequestParam
			@Schema(description = "Nome do vídeo", example = "video.mp4", requiredMode = Schema.RequiredMode.REQUIRED) String videoName) {
		try {
			var report = getAuthUserLastExistingReportController.getAuthUserLastExistingReport(requestId, videoName);

			log.info("request_completed");

			return ResponseEntity.ok(report);

		} catch (Exception e) {
			log.error("request_error", e);
			throw e;

		} finally {
			CanonicalContext.clear();
		}
	}
}
