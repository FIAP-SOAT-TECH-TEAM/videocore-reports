package com.soat.fiap.videocore.reports.infrastructure.out.persistence.blobstorage.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.SasProtocol;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.reports.infrastructure.common.config.azure.blobstorage.AzureBlobStorageProperties;
import com.soat.fiap.videocore.reports.infrastructure.common.config.environment.EnvironmentProperties;
import com.soat.fiap.videocore.reports.infrastructure.common.source.VideoDataSource;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.blobstorage.mapper.VideoBlobMapper;

import lombok.RequiredArgsConstructor;

/** Repositório de acesso a vídeos no Azure Blob Storage. */
@Component @RequiredArgsConstructor
public class AzureBlobStorageRepository implements VideoDataSource {

	private final AzureBlobStorageProperties properties;
	private final EnvironmentProperties environmentProperties;
	private final VideoBlobMapper videoBlobMapper;

	/** Recupera metadados do vídeo a partir da URL do blob. */
	@Override
	public VideoDto getVideo(String videoUrl) {

		var blobClient = new BlobClientBuilder().endpoint(videoUrl)
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

	/**
	 * Obtém a url de download para as imagens capturadas de um dado vídeo,
	 * protegida por SAS Token
	 *
	 * @param userId
	 *            ID do usuário responsável por encaminhar o vídeo
	 * @param requestId
	 *            ID da requisição
	 * @param videoName
	 *            Nome do vídeo
	 * @param expirationMinuteTime
	 *            Minutos de expiração para a URL de download
	 * @return a URL para download das imagens do vídeo, ou nulo caso o blob não
	 *         exista
	 */
	@Override
	public String getVideoImagesDownloadUrl(String userId, String requestId, String videoName,
			long expirationMinuteTime) {
		var blobName = String.format("%s/%s/%s.zip", userId, requestId, videoName);

		var blobClient = new BlobClientBuilder().connectionString(properties.getConnectionString())
				.containerName(properties.getImageContainerName())
				.blobName(blobName)
				.buildClient();

		if (!blobClient.exists())
			return null;

		var permissions = new BlobSasPermission().setReadPermission(true);

		var expiryTime = OffsetDateTime.now().plusMinutes(expirationMinuteTime);

		var sasValues = new BlobServiceSasSignatureValues(expiryTime, permissions)
				.setContentDisposition("attachment; filename=\"" + removeExtension(videoName) + ".zip\"")
				.setContentType("application/zip");

		if (environmentProperties.isProd())
			sasValues.setProtocol(SasProtocol.HTTPS_ONLY);

		var sasToken = blobClient.generateSas(sasValues);

		return blobClient.getBlobUrl() + "?" + sasToken;
	}

	/**
	 * Gera uma URL SAS para upload de um arquivo no Azure Blob Storage.
	 *
	 * <p>
	 * A URL retornada permite apenas a operação de escrita (upload) no blob, com
	 * tempo de expiração definido em minutos. O caminho do blob é construído a
	 * partir do {@code userId} e {@code requestId}, garantindo isolamento lógico
	 * por usuário e requisição.
	 *
	 * @param userId
	 *            ID do usuário responsável pelo upload do arquivo
	 * @param requestId
	 *            ID da requisição associada ao upload
	 * @param videoNames
	 *            Nome dos vídeos (arquivos com extensão)
	 * @param expirationMinuteTime
	 *            Minutos de expiração da URL SAS
	 * @return URLs SAS para upload dos vídeos
	 */
	@Override
	public List<String> getVideoUploadUrls(String userId, String requestId, List<String> videoNames,
			long expirationMinuteTime) {
		var permissions = new BlobSasPermission().setCreatePermission(true).setWritePermission(true);
		var expiryTime = OffsetDateTime.now().plusMinutes(expirationMinuteTime);
		var sasValues = new BlobServiceSasSignatureValues(expiryTime, permissions);

		if (environmentProperties.isProd())
			sasValues.setProtocol(SasProtocol.HTTPS_ONLY);

		return videoNames.stream().map(videoName -> {
			var blobName = String.format("%s/%s/%s", userId, requestId, videoName);

			var blobClient = new BlobClientBuilder().connectionString(properties.getConnectionString())
					.containerName(properties.getVideoContainerName())
					.blobName(blobName)
					.buildClient();

			var sasToken = blobClient.generateSas(sasValues);
			return blobClient.getBlobUrl() + "?" + sasToken;
		}).toList();
	}

	/**
	 * Remove a extensão do nome de um arquivo, considerando apenas o último ponto.
	 *
	 * <p>
	 * Exemplo:
	 *
	 * <ul>
	 * <li>{@code video.mp4} → {@code video}
	 * <li>{@code meu.video.final.mkv} → {@code meu.video.final}
	 * <li>{@code video} → {@code video}
	 * </ul>
	 *
	 * @param fileName
	 *            nome do arquivo, com ou sem extensão
	 * @return nome do arquivo sem a extensão
	 */
	private String removeExtension(String fileName) {
		var lastDot = fileName.lastIndexOf('.');
		return (lastDot > 0) ? fileName.substring(0, lastDot) : fileName;
	}
}
