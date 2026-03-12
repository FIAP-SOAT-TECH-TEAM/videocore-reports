package com.soat.fiap.videocore.reports.infrastructure.in.http.response.swagger;

import java.util.List;

import com.soat.fiap.videocore.reports.infrastructure.in.http.response.PaginationResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.ReportResponse;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Especialização de {@link PaginationResponse} para documentação Swagger
 * contendo {@link ReportResponse}.
 */
@Schema(name = "ReportPaginationResponse", description = "Resposta paginada contendo os reportes")
public class ReportPaginationResponse extends PaginationResponse<ReportResponse> {

	public ReportPaginationResponse(int page, int size, long totalElements, int totalPages, boolean hasPrevious,
			boolean hasNext, List<ReportResponse> content) {

		super(page, size, totalElements, totalPages, hasPrevious, hasNext, content);
	}
}
