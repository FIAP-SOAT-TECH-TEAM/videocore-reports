package com.soat.fiap.videocore.reports.core.interfaceadapters.presenter;

import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoImagesDownloadUrlResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoUploadUrlResponse;
import org.mapstruct.Mapper;

/**
 * Presenter responsável por converter objetos relacionados a respostas HTTPs das imagens capturadas de um vídeo.
 */
@Mapper(componentModel = "spring")
public interface ImagePresenter {

    /**
     * Converte uma URL de download em {@link VideoImagesDownloadUrlResponse}.
     *
     * @param url URL de download das imagens do vídeo
     * @return response contendo a URL
     */
    VideoImagesDownloadUrlResponse toResponse(String url);

    /**
     * Converte uma URL de upload em {@link VideoUploadUrlResponse}.
     *
     * @param url URL de upload do vídeo
     * @return response contendo a URL
     */
    VideoUploadUrlResponse toUploadResponse(String url);
}