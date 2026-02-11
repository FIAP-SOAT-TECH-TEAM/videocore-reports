package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoImagesDownloadUrlUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ImagePresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoImagesDownloadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Controller responsável por orquestrar a recuperação da URL de download
 * das imagens capturadas de um vídeo, enviado pelo usuário autenticado.
 */
@Component
@RequiredArgsConstructor
public class GetAuthUserVideoImagesDownloadUrlController {

    private final GetAuthUserVideoImagesDownloadUrlUseCase getAuthUserVideoImagesDownloadUrlUseCase;
    private final ImagePresenter imagePresenter;

    /**
     * Retorna a URL de download das imagens de um vídeo, enviado pelo usuário autenticado
     *
     * @param requestId identificador da requisição
     * @param videoName nome do vídeo
     * @return URL para download das imagens do vídeo
     */
    @WithSpan(name = "controller.get.authenticated.user.video.images.download.url")
    public VideoImagesDownloadUrlResponse getVideoImagesDownloadUrl(String requestId, String videoName) {
        var videoUrl = getAuthUserVideoImagesDownloadUrlUseCase.getVideoImagesDownloadUrl(requestId, videoName);

        return imagePresenter.toResponse(videoUrl);
    }
}