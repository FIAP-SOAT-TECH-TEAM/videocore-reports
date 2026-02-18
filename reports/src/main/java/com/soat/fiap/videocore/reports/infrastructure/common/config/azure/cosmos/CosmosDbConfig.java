package com.soat.fiap.videocore.reports.infrastructure.common.config.azure.cosmos;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração de integração com Azure Cosmos DB para Spring Data.
 * Estende a configuração base do Spring Data Cosmos para fornecer o
 * nome do banco e o construtor de cliente.
 */
@Configuration
@RequiredArgsConstructor
@EnableCosmosRepositories(basePackages = "com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.repository")
@Profile("!test")
public class CosmosDbConfig extends AbstractCosmosConfiguration {

    @Value("${azure.cosmos.endpoint}")
    private String endpoint;

    @Value("${azure.cosmos.database}")
    private String database;

    @Value("${azure.cosmos.key}")
    private String key;

    /**
     * Retorna o nome do banco de dados Cosmos configurado.
     * Este método é usado pelo Spring Data Cosmos para definição do contexto.
     *
     * @return nome do banco de dados
     */
    @Override
    protected String getDatabaseName() {
        return database;
    }

    /**
     * Gera o bean {@link CosmosClientBuilder} usado para criar
     * instâncias de cliente Cosmos DB com os valores de endpoint e chave.
     * @return builder configurado de cliente Cosmos
     */
    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        return new CosmosClientBuilder()
                .endpoint(endpoint)
                .key(key)
                .directMode();
    }
}