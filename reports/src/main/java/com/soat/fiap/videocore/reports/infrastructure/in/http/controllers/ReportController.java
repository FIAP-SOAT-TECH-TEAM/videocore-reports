package com.soat.fiap.videocore.reports.infrastructure.in.http.controllers;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetUserLastReportsController;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para gerenciamento de reportes
 */
@RestController
@RequestMapping("/")
@Tag(name = "Reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final GetUserLastReportsController getUserLastReportsController;

    @GetMapping("/{userId}")
    @Operation(
            summary = "Buscar reports por ID do usuário",
            description = "Retorna todos os reports associados a um usuário específico pelo seu ID",
            tags = { "Reports" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reports encontrados",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ReportResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quando o ID do usuário estiver vazio ou não for informado",
                    content = @Content
            )
    })
    public ResponseEntity<List<ReportResponse>> getReports(@PathVariable String userId) {
        try {
            CanonicalContext.add("user_id", userId);

            var reports = getUserLastReportsController.getAllUserReports(userId);

            log.info("request_completed");

            return ResponseEntity.ok(reports);
        }
        finally {
            CanonicalContext.clear();
        }
    }
}
