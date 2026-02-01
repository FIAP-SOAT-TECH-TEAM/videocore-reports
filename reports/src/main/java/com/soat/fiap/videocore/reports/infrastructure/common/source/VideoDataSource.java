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
}