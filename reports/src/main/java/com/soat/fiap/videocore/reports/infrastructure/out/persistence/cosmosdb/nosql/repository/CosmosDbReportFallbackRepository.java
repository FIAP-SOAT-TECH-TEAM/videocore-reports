package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.azure.cosmos.models.CosmosPatchItemRequestOptions;
import com.azure.cosmos.models.CosmosPatchOperations;
import com.azure.cosmos.models.PartitionKey;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity.ReportEntity;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.projection.ReportTimeProjection;

/**
 * Configuração de Fallback para {@link CosmosDbReportRepository}.
 *
 * <p>
 * *
 */
@Configuration
public class CosmosDbReportFallbackRepository {

	/**
	 * Bean de fallback para {@link CosmosDbReportRepository}.
	 *
	 * <p>
	 * É registrado apenas se não houver outro bean do tipo
	 * {@link CosmosDbReportRepository} disponível no contexto Spring.
	 *
	 * @return uma implementação vazia de {@link CosmosDbReportRepository}, para
	 *         fins de teste ou tasks especificas
	 */
	@Bean @ConditionalOnMissingBean(CosmosDbReportRepository.class)
	public CosmosDbReportRepository cosmosDbReportRepository() {
		return new CosmosDbReportRepository() {

			@Override
			public Iterable<ReportEntity> findAll(Sort sort) {
				return null;
			}

			@Override
			public Page<ReportEntity> findAll(Pageable pageable) {
				return null;
			}

			@Override
			public <S extends ReportEntity> S save(S entity) {
				return null;
			}

			@Override
			public <S extends ReportEntity> Iterable<S> saveAll(Iterable<S> entities) {
				return null;
			}

			@Override
			public Optional<ReportEntity> findById(String s) {
				return Optional.empty();
			}

			@Override
			public boolean existsById(String s) {
				return false;
			}

			@Override
			public Iterable<ReportEntity> findAll() {
				return null;
			}

			@Override
			public Iterable<ReportEntity> findAllById(Iterable<String> strings) {
				return null;
			}

			@Override
			public long count() {
				return 0;
			}

			@Override
			public void deleteById(String s) {
			}

			@Override
			public void delete(ReportEntity entity) {
			}

			@Override
			public void deleteAllById(Iterable<? extends String> strings) {
			}

			@Override
			public void deleteAll(Iterable<? extends ReportEntity> entities) {
			}

			@Override
			public void deleteAll() {
			}

			@Override
			public Optional<ReportEntity> findById(String s, PartitionKey partitionKey) {
				return Optional.empty();
			}

			@Override
			public void deleteById(String s, PartitionKey partitionKey) {
			}

			@Override
			public <S extends ReportEntity> S save(String s, PartitionKey partitionKey, Class<S> domainType,
					CosmosPatchOperations patchOperations) {
				return null;
			}

			@Override
			public <S extends ReportEntity> S save(String s, PartitionKey partitionKey, Class<S> domainType,
					CosmosPatchOperations patchOperations, CosmosPatchItemRequestOptions options) {
				return null;
			}

			@Override
			public Iterable<ReportEntity> findAll(PartitionKey partitionKey) {
				return null;
			}

			@Override
			public Optional<ReportEntity> findByUserIdAndRequestIdAndVideoNameAndPercentStatusProcess(String userId,
					String requestId, String videoName, Double percentStatusProcess) {
				return Optional.empty();
			}

			@Override
			public Optional<ReportEntity> findTopByUserIdAndRequestIdAndVideoNameOrderByReportTimeDesc(String userId,
					String requestId, String videoName) {
				return Optional.empty();
			}

			@Override
			public List<ReportTimeProjection> findLatestReportsTimesByUser(String userId) {
				return List.of();
			}

			@Override
			public List<ReportEntity> findByReportTimeIn(List<String> reportTimes) {
				return List.of();
			}
		};
	}
}
