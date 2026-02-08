package com.soat.fiap.videocore.reports.infrastructure.common.source;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;

import java.util.List;

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
     * @return a URl para download das imagens do vídeo, ou nulo caso o arquivo de imagens não exista
     */
    String getVideoImagesDownloadUrl(String userId, String requestId, String videoName, long expirationMinuteTime);

    /**
     * Obtém uma URL para upload de um vídeo para processamento.
     * <p>
     * A URL retornada permite apenas operações de criação e escrita,
     * sendo válida por um período definido em minutos. O caminho do arquivo
     * é construído a partir do {@code userId} e {@code requestId}.
     *
     * @param userId ID do usuário responsável pelo upload do vídeo
     * @param requestId ID da requisição associada ao upload
     * @param videoNames Nome dos vídeos (arquivos com extensão)
     * @param expirationMinuteTime Minutos de expiração da URL
     *
     * @return as URLs para upload dos vídeos
     */
    List<String> getVideoUploadUrls(String userId, String requestId, List<String> videoNames, long expirationMinuteTime);

}