package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoUploadUrlUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ImagePresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoUploadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Controller responsável por orquestrar a recuperação da URL de upload
 * de um vídeo para processamento, para o usuário autenticado.
 */
@Component
@RequiredArgsConstructor
public class GetAuthUserVideoUploadUrlController {

    private final GetAuthUserVideoUploadUrlUseCase getAuthUserVideoUploadUrlUseCase;
    private final ImagePresenter imagePresenter;

    /**
     * Retorna a URL de upload de um vídeo para o usuário autenticado
     *
     * @param videoName Nome do vídeo (arquivo com extensão)
     *
     * @return URL para upload do vídeo
     */
    @WithSpan(name = "controller.get.authenticated.user.video.upload.url")
    public VideoUploadUrlResponse getVideoUploadUrl(String videoName) {
        var uploadUrl = getAuthUserVideoUploadUrlUseCase.getVideoUploadUrl(videoName);

        return imagePresenter.toUploadResponse(uploadUrl);
    }
}
