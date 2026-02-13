package com.soat.fiap.videocore.reports.infrastructure.common.hints.azure.cosmos;

import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.repository.support.CosmosEntityInformation;
import com.azure.spring.data.cosmos.repository.support.SimpleCosmosRepository;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

/**
 * Registrar de {@link RuntimeHints} para suporte a reflexão em tempo de execução
 * quando a aplicação é compilada como imagem nativa (GraalVM).
 *
 * <p>
 * Registra explicitamente construtores e métodos necessários das classes
 * {@link SimpleCosmosRepository}, {@link CosmosEntityInformation} e
 * {@link CosmosTemplate}, evitando falhas de instanciação via reflexão
 * durante a inicialização do Spring Data Cosmos em ambiente AOT.
 * </p>
 */
public class CosmosHints implements RuntimeHintsRegistrar {

    /**
     * Registra os hints de reflexão necessários para o correto funcionamento
     * dos repositórios do Spring Data Cosmos em modo nativo.
     *
     * @param hints estrutura utilizada pelo Spring AOT para registrar metadados
     *              necessários em tempo de execução
     * @param classLoader class loader da aplicação
     */
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        hints.reflection().registerType(
                SimpleCosmosRepository.class,
                builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_METHODS)
        );

        hints.reflection().registerType(
                CosmosEntityInformation.class,
                builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
        );

        hints.reflection().registerType(
                CosmosTemplate.class,
                builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
        );

        hints.reflection().registerType(
                TypeReference.of("com.azure.cosmos.implementation.directconnectivity.rntbd.RntbdToken$PropertyFilter"),
                builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
        );
    }
}