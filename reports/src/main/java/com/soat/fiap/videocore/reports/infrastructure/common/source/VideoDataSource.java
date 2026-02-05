package com.soat.fiap.videocore.reports.infrastructure.common.source;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;

/**
 * Fonte de dados para obtenção de um vídeo.
 */
public interface VideoDataSource {

    /**
     * Obtém um vídeo a partir de sua URL.
     *
     * @param videoUrl URL do vídeo
     * @return o vídeo
     */
    VideoDto getVideo(String videoUrl);

    /**
     * Obtém a url de download para as imagens capturadas de um dado vídeo
     *
     * @param userId ID do usuário responsável por encaminhar o vídeo
     * @param requestId ID da requisição
     * @param videoName Nome do vídeo
     * @param expirationMinuteTime Minutos de expiração para a URL de download
     *
     * @return a URl para download das imagens do vídeo
     */
    String getVideoImagesDownloadUrl(String userId, String requestId, String videoName, long expirationMinuteTime);
}