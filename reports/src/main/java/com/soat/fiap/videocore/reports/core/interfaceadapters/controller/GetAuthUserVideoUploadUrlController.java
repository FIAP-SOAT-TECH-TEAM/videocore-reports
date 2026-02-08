package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoUploadUrlUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ImagePresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoUploadUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
     * @param videoNames Nome dos vídeos (arquivos com extensão)
     *
     * @return URLs para upload dos vídeos
     */
    @WithSpan(name = "controller.get.authenticated.user.video.upload.url")
    public List<VideoUploadUrlResponse> getVideoUploadUrl(List<String> videoNames) {
        var uploadUrl = getAuthUserVideoUploadUrlUseCase.getVideoUploadUrl(videoNames);

        return imagePresenter.toUploadResponse(uploadUrl);
    }
}
