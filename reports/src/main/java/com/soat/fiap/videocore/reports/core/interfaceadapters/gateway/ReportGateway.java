package com.soat.fiap.videocore.reports.core.interfaceadapters.gateway;

import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.interfaceadapters.mapper.ReportMapper;
import com.soat.fiap.videocore.reports.infrastructure.common.source.ReportDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public Report save(Report report) {
        var dto = reportMapper.toDto(report);
        var savedDto = reportDataSource.save(dto);

        return reportMapper.toModel(savedDto);
    }
}