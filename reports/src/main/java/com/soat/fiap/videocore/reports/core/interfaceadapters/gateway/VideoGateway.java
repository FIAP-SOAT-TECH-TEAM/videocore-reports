package com.soat.fiap.videocore.reports.core.interfaceadapters.gateway;

import java.util.List;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.reports.infrastructure.common.source.VideoDataSource;

import lombok.RequiredArgsConstructor;

/**
 * Gateway responsável por abstrair o acesso a vídeos armazenados externamente.
 * Atua como intermediário entre a camada de aplicação e a fonte de dados.
 */
@Component @RequiredArgsConstructor
public class VideoGateway {

	private final VideoDataSource videoDataSource;

	/**
	 * Recupera um o vídeo a partir de uma URL.
	 *
	 * @param videoUrl
	 *            URL completa do vídeo
	 * @return O vídeo, ou nulo se não encontrado
	 */
	@WithSpan(name = "gateway.get.video")
	public VideoDto getVideo(String videoUrl) {
		return videoDataSource.getVideo(videoUrl);
	}

	/**
	 * Recupera a url de download para as imagens capturadas de um dado vídeo
	 *
	 * @param userId
	 *            ID do usuário responsável por encaminhar o vídeo
	 * @param requestId
	 *            ID da requisição
	 * @param videoName
	 *            Nome do vídeo
	 * @param expirationMinuteTime
	 *            Minutos de expiração para a URL de download
	 * @return a URl para download das imagens do vídeo, ou nulo caso o arquivo de
	 *         imagens não exista
	 */
	@WithSpan(name = "gateway.get.video.image.download.url")
	public String getVideoImagesDownloadUrl(String userId, String requestId, String videoName,
			long expirationMinuteTime) {
		return videoDataSource.getVideoImagesDownloadUrl(userId, requestId, videoName, expirationMinuteTime);
	}

	/**
	 * Obtém uma URL para upload de um vídeo para processamento.
	 *
	 * <p>
	 * A URL retornada permite apenas operações de criação e escrita, sendo válida
	 * por um período definido em minutos. O caminho do arquivo é construído a
	 * partir do {@code userId} e {@code
	 * requestId}.
	 *
	 * @param userId
	 *            ID do usuário responsável pelo upload do vídeo
	 * @param requestId
	 *            ID da requisição associada ao upload
	 * @param videoNames
	 *            Nome dos vídeos (arquivos com extensão)
	 * @param expirationMinuteTime
	 *            Minutos de expiração da URL
	 * @return as URLs para upload dos vídeos
	 */
	@WithSpan(name = "gateway.get.video.upload.url")
	public List<String> getVideoUploadUrl(String userId, String requestId, List<String> videoNames,
			long expirationMinuteTime) {
		return videoDataSource.getVideoUploadUrls(userId, requestId, videoNames, expirationMinuteTime);
	}
}
