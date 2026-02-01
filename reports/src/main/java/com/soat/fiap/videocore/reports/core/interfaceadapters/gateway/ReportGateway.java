package com.soat.fiap.videocore.reports.core.interfaceadapters.gateway;

import com.soat.fiap.videocore.reports.common.observability.trace.TraceContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.core.interfaceadapters.mapper.ReportMapper;
import com.soat.fiap.videocore.reports.infrastructure.common.source.ReportDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Gateway responsável por persistir objetos {@link Report} usando a camada de persistência subjacente.
 */
@Component
@RequiredArgsConstructor
public class ReportGateway {
    private final ReportDataSource reportDataSource;
    private final ReportMapper reportMapper;

    /**
     * Persiste um {@link Report} no datasource e retorna a versão salva.
     *
     * @param report objeto {@link Report} a ser salvo
     * @return o {@link Report} salvo, mapeado do DTO retornado pelo datasource
     */
    @WithSpan(name = "gateway.save.report")
    public Report save(Report report) {
        var dto = reportMapper.toDto(report);
        var savedDto = reportDataSource.save(dto);

        return reportMapper.toModel(savedDto);
    }

    /**
     * Recupera um reporte já existente a partir de {@code userId}, {@code requestId}, {@code videoName}
     * e {@code percentStatusProcess}.
     *
     * @param userId               identificador do usuário (partition key)
     * @param requestId            identificador da requisição
     * @param videoName            nome do vídeo
     * @param percentStatusProcess percentual do status do processamento
     * @return {@link Optional} com o {@link Report} encontrado, ou vazio se não existir
     */
    @WithSpan(name = "gateway.get.report.existing")
    public Optional<Report> getExistingReport(UUID userId, UUID requestId, String videoName, Double percentStatusProcess) {
        var dto = reportDataSource.getExistingReport(userId, requestId, videoName, percentStatusProcess);

        TraceContext.addEvent("report.object", dto);

        return dto.map(reportMapper::toModel);
    }


    /**
     * Recupera o último reporte persistido para {@code userId}, {@code requestId} e {@code videoName},
     *
     * @param userId    identificador do usuário (partition key)
     * @param requestId identificador da requisição
     * @param videoName nome do vídeo
     * @return {@link Optional} com o {@link ReportDto} encontrado, ou vazio se não existir
     */
    @WithSpan(name = "gateway.get.last.report.existing")
    public Optional<Report> getLastExistingReport(UUID userId, UUID requestId, String videoName) {

        var dto = reportDataSource.getLastExistingReport(userId, requestId, videoName);

        TraceContext.addEvent("report.object", dto);

        return dto.map(reportMapper::toModel);

    }
}