package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.common.source.ReportDataSource;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper.ReportEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

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

    /**
     * Recupera um reporte já existente a partir de {@code userId}, {@code requestId}, {@code videoName}
     * e {@code percentStatusProcess}.
     *
     * @param userId               identificador do usuário (partition key)
     * @param requestId            identificador da requisição
     * @param videoName            nome do vídeo
     * @param percentStatusProcess percentual do status do processamento
     * @return {@link Optional} com o {@link ReportDto} encontrado, ou vazio se não existir
     */
    @Override @Transactional(readOnly = true)
    public Optional<ReportDto> getExistingReport(UUID userId, UUID requestId, String videoName, Double percentStatusProcess) {

        return cosmosDbReportRepository
                .findByUserIdAndRequestIdAndVideoNameAndPercentStatusProcess(userId.toString(), requestId.toString(), videoName, percentStatusProcess)
                .map(reportEntityMapper::toDto);
    }

    /**
     * Recupera o último reporte persistido para {@code userId}, {@code requestId} e {@code videoName},
     * utilizando o atributo nativo {@code _ts} do Cosmos DB para determinar o registro mais recente.
     *
     * @param userId    identificador do usuário (partition key)
     * @param requestId identificador da requisição
     * @param videoName nome do vídeo
     * @return {@link Optional} com o {@link ReportDto} encontrado, ou vazio se não existir
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReportDto> getLastExistingReport(UUID userId, UUID requestId, String videoName) {

        return cosmosDbReportRepository
                .findTopByUserIdAndRequestIdAndVideoNameOrderByReportTimeDesc(userId.toString(), requestId.toString(), videoName)
                .map(reportEntityMapper::toDto);
    }


}