package com.soat.fiap.videocore.reports.infrastructure.in.http.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthenticatedUserLastReportsController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetReportByIdController;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

	private final GetAuthenticatedUserLastReportsController getAuthenticatedUserLastReportsController;
	private final GetReportByIdController getReportByIdController;

	@GetMapping("/latest")
	@Operation(summary = "Obter reports mais recentes do usuário autenticado", description = "Retorna a lista de reportes mais recentes dos videos enviados pelo usuário autenticado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reports encontrados", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ReportResponse.class)))),
			@ApiResponse(responseCode = "401", description = "Quando não for possível obter o ID do usuário autenticado (Header HTTTP Auth-Subject)", content = @Content)})
	public ResponseEntity<List<ReportResponse>> getAuthUserLastReports() {
		try {
			var reports = getAuthenticatedUserLastReportsController.getAuthenticatedUserLastReports();

			log.info("request_completed");

			return ResponseEntity.ok(reports);
		} catch (Exception e) {
			log.error("request_error", e);
			throw e;
		} finally {
			CanonicalContext.clear();
		}
	}

	@GetMapping("/{reportId}")
	@Operation(summary = "Obter report por ID", description = "Retorna um reporte específico pelo seu identificador único")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reporte encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportResponse.class))),
			@ApiResponse(responseCode = "400", description = "ID do reporte inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Reporte não encontrado", content = @Content),
			@ApiResponse(responseCode = "401", description = "Quando não for possível obter o ID do usuário autenticado (Header HTTTP Auth-Subject)", content = @Content),
			@ApiResponse(responseCode = "403", description = "Quando o reporte não pertencer ao usuário autenticado", content = @Content)})
	public ResponseEntity<ReportResponse> getReportById(@PathVariable(required = true)
	@Schema(description = "Identificador único do reporte", example = "3c29043a-f5b0-482b-ad20-ff6c7310d9ee", requiredMode = Schema.RequiredMode.REQUIRED) String reportId) {
		try {
			var report = getReportByIdController.getReportById(reportId);

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
