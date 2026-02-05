package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.common.source.ReportDataSource;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper.ReportEntityMapper;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection.IdProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
     * @param userId               identificador do usuário
     * @param requestId            identificador da requisição
     * @param videoName            nome do vídeo
     * @param percentStatusProcess percentual do status do processamento
     * @return {@link Optional} com o {@link ReportDto} encontrado, ou vazio se não existir
     */
    @Override @Transactional(readOnly = true)
    public Optional<ReportDto> getExistingReport(String userId, String requestId, String videoName, Double percentStatusProcess) {

        return cosmosDbReportRepository
                .findByUserIdAndRequestIdAndVideoNameAndPercentStatusProcess(userId, requestId, videoName, percentStatusProcess)
                .map(reportEntityMapper::toDto);
    }

    /**
     * Recupera o último reporte persistido para {@code userId}, {@code requestId} e {@code videoName},
     * utilizando o atributo nativo {@code _ts} do Cosmos DB para determinar o registro mais recente.
     *
     * @param userId    identificador do usuário
     * @param requestId identificador da requisição
     * @param videoName nome do vídeo
     * @return {@link Optional} com o {@link ReportDto} encontrado, ou vazio se não existir
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReportDto> getLastExistingReport(String userId, String requestId, String videoName) {

        return cosmosDbReportRepository
                .findTopByUserIdAndRequestIdAndVideoNameOrderByReportTimeDesc(userId, requestId, videoName)
                .map(reportEntityMapper::toDto);
    }

    /**
     * Recupera os reportes mais recentes dos videos enviados por um usuário.
     *
     * <p><b>Aviso:</b> Cosmos DB não suporta non-correlated queries.
     * Por isso, primeiro buscamos apenas os IDs dos últimos reportes, e depois os reportes efetivamente.
     * <a href="https://learn.microsoft.com/en-us/cosmos-db/query/subquery#types-of-subqueries">How to use subquery (SQL) in azure cosmos db</a>
     * <a href="https://learn.microsoft.com/en-us/answers/questions/528514/how-to-use-subquery-(sql)-in-azure-cosmos-db">Types of subqueries</a>
     *
     * @param userId identificador do usuário
     * @return lista de reportes encontrados (pode ser vazia)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReportDto> getLastReportsByUserId(String userId) {
        var ids = cosmosDbReportRepository.findLatestReportsIdsByUser(userId)
                .stream()
                .map(IdProjection::id)
                .toList();

        if (ids.isEmpty())
            return List.of();

        return cosmosDbReportRepository
                .findByIdIn(ids)
                .stream()
                .map(reportEntityMapper::toDto)
                .toList();
    }
}