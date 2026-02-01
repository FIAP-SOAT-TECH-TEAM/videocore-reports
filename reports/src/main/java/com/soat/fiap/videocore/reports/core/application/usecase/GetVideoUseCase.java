package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ProcessReportException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por recuperar um vídeo a partir de sua URL.
 */
@Component
@RequiredArgsConstructor
public class GetVideoUseCase {

    private final VideoGateway videoGateway;

    /**
     * Recupera o {@link VideoDto} do vídeo a partir de uma URL.
     *
     * @param videoUrl URL completa do vídeo
     * @return o vídeo
     * @throws ProcessReportException caso o vídeo não seja encontrado (retorno nulo)
     */
    @WithSpan(name = "usecase.get.video")
    public VideoDto getVideo(String videoUrl) {
        var video = videoGateway.getVideo(videoUrl);

        if (video == null) {
            throw new ProcessReportException("Video não encontrado para URL: " + videoUrl);
        }

        return video;
    }
}