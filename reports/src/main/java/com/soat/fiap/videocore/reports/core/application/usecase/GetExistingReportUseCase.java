package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.ReportGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso responsável por buscar um reporte já existente a partir de chaves de negócio.
 */
@Component
@RequiredArgsConstructor
public class GetExistingReportUseCase {

    private final ReportGateway reportGateway;

    /**
     * Busca um {@link Report} existente pelo {@code userId}, {@code requestId}, {@code videoName}
     * e {@code percentStatusProcess}.
     *
     * @param userId               identificador do usuário
     * @param requestId            identificador da requisição
     * @param videoName            nome do vídeo
     * @param percentStatusProcess percentual do status do processamento
     * @return {@link Optional} com o reporte encontrado, ou vazio se não existir
     */
    @WithSpan(name = "usecase.get.report.existing")
    public Optional<Report> getExistingReport(UUID userId, UUID requestId, String videoName, Double percentStatusProcess) {
        return reportGateway.getExistingReport(userId, requestId, videoName, percentStatusProcess);
    }
}