package com.soat.fiap.videocore.reports.core.interfaceadapters.gateway;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.reports.infrastructure.common.source.VideoDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Gateway responsável por abstrair o acesso a vídeos armazenados externamente.
 * Atua como intermediário entre a camada de aplicação e a fonte de dados.
 */
@Component
@RequiredArgsConstructor
public class VideoGateway {

    private final VideoDataSource videoDataSource;

    /**
     * Recupera um o vídeo a partir de uma URL.
     *
     * @param videoUrl URL completa do vídeo
     * @return O vídeo, ou nulo se não encontrado
     */
    @WithSpan(name = "gateway.get.video")
    public VideoDto getVideo(String videoUrl) {

        return videoDataSource.getVideo(videoUrl);
    }
}