package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetVideoImagesDownloadUrlUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ImagePresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoImagesDownloadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Controller responsável por orquestrar a recuperação da URL de download
 * das imagens capturadas de um vídeo.
 */
@Component
@RequiredArgsConstructor
public class GetVideoImagesDownloadUrlController {

    private final GetVideoImagesDownloadUrlUseCase getVideoImagesDownloadUrlUseCase;
    private final ImagePresenter imagePresenter;

    /**
     * Retorna a URL de download das imagens de um vídeo.
     *
     * @param userId identificador do usuário
     * @param requestId identificador da requisição
     * @param videoName nome do vídeo
     * @return URL para download das imagens do vídeo
     */
    @WithSpan(name = "controller.get.video.images.download.url")
    public VideoImagesDownloadUrlResponse getVideoImagesDownloadUrl(String userId, String requestId, String videoName) {
        var videoUrl = getVideoImagesDownloadUrlUseCase.getVideoImagesDownloadUrl(userId, requestId, videoName);

        return imagePresenter.toResponse(videoUrl);
    }
}