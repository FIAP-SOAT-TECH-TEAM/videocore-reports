package com.soat.fiap.videocore.reports.infrastructure.out.persistence.blobstorage.mapper;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;
import java.util.UUID;

/**
 * Mapper MapStruct para converter metadados do Blob Storage em {@link VideoDto}.
 */
@Mapper(componentModel = "spring", imports = {UUID.class, Integer.class})
public interface VideoBlobMapper {

    /**
     * Converte o metadata do blob e o nome do blob em {@link VideoDto}.
     *
     * @param metadata mapa de metadados do blob (ex.: user_id, request_id)
     * @param blobName nome/caminho do blob
     * @return DTO com os dados extraídos
     */
    @Mapping(
            target = "videoName",
            expression = "java(extractVideoName(blobName))"
    )
    @Mapping(
            target = "userId",
            expression = "java(UUID.fromString(metadata.get(\"user_id\")))"
    )
    @Mapping(
            target = "requestId",
            expression = "java(UUID.fromString(metadata.get(\"request_id\")))"
    )
    @Mapping(
            target = "minuteFrameCut",
            expression = "java(Long.parseLong(metadata.get(\"frame_cut\")))"
    )
    VideoDto toDto(Map<String, String> metadata, String blobName);

    /**
     * Extrai o nome do vídeo a partir do caminho do blob.
     *
     * @param blobName nome/caminho do blob
     * @return nome do arquivo (após a última '/'), ou o próprio valor se não houver '/'
     */
    default String extractVideoName(String blobName) {
        if (blobName == null) {
            return null;
        }
        var lastSlash = blobName.lastIndexOf('/');

        return lastSlash >= 0 ? blobName.substring(lastSlash + 1) : blobName;
    }
}