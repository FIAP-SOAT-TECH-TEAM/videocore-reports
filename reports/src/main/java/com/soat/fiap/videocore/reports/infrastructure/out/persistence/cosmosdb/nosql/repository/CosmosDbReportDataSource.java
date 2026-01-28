package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.common.source.ReportDataSource;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper.ReportEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação de {@link ReportDataSource} usando Cosmos DB.
 */
@Component
@RequiredArgsConstructor
public class CosmosDbReportDataSource implements ReportDataSource {
    private final CosmosDbReportRepository cosmosDbReportRepository;
    private final ReportEntityMapper reportEntityMapper;

    /**
     * Persiste o {@link ReportDto} no Cosmos DB e retorna a versão salva.
     *
     * @param reportDto reporte a ser salvo
     * @return reporte persistido
     */
    @Override @Transactional
    public ReportDto save(ReportDto reportDto) {
        var entity = reportEntityMapper.toEntity(reportDto);
        var savedEntity = cosmosDbReportRepository.save(entity);

        return reportEntityMapper.toDto(savedEntity);
    }
}