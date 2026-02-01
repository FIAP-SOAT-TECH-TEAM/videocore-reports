package com.soat.fiap.videocore.reports.core.interfaceadapters.controller;

import com.soat.fiap.videocore.reports.core.application.usecase.GetVideoUseCase;
import com.soat.fiap.videocore.reports.core.application.usecase.ProcessVideoErrorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Controller responsável por orquestrar o fluxo de processamento de erro de um vídeo.
 * <p>
 * Busca os dados do vídeo a partir da URL informada e delega o tratamento de erro ao caso de uso.
 */
@Component
@RequiredArgsConstructor
public class ProcessVideoErrorController {

    private final GetVideoUseCase getVideoUseCase;
    private final ProcessVideoErrorUseCase processVideoErrorUseCase;

    /**
     * Processa um erro relacionado a um vídeo a partir da sua URL.
     *
     * @param videoUrl URL do vídeo a ser consultado e processado.
     */
    public void processVideoError(String videoUrl) {
        var dto = getVideoUseCase.getVideo(videoUrl);

        processVideoErrorUseCase.processVideoError(dto);
    }
}