package com.soat.fiap.videocore.reports.infrastructure.out.persistence.blobstorage.repository;

import com.azure.storage.blob.BlobClientBuilder;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.reports.infrastructure.common.config.azure.blobstorage.AzureBlobStorageProperties;
import com.soat.fiap.videocore.reports.infrastructure.common.source.VideoDataSource;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.blobstorage.mapper.VideoBlobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * Repositório de acesso a vídeos no Azure Blob Storage.
 */
@Component
@RequiredArgsConstructor
public class AzureBlobStorageRepository implements VideoDataSource {

    private final AzureBlobStorageProperties properties;
    private final VideoBlobMapper videoBlobMapper;

    /**
     * Recupera metadados do vídeo a partir da URL do blob.
     */
    @Override
    public VideoDto getVideo(String videoUrl) {

        var blobClient = new BlobClientBuilder()
                .endpoint(videoUrl)
                .connectionString(properties.getConnectionString())
                .buildClient();

        if (!blobClient.exists()) {
            return null;
        }

        var blobName = blobClient.getBlobName();
        var properties = blobClient.getProperties();
        var metadata = properties.getMetadata();

        return videoBlobMapper.toDto(metadata, blobName);
    }
}