package com.soat.fiap.videocore.reports.core.interfaceadapters.presenter;

import java.util.List;

import org.mapstruct.Mapper;

import com.soat.fiap.videocore.reports.core.application.output.VideoUploadUrlOutput;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoImagesDownloadUrlResponse;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoUploadUrlResponse;

/**
 * Presenter responsável por converter objetos relacionados a respostas HTTPs
 * das imagens capturadas de um vídeo.
 */
@Mapper(componentModel = "spring")
public interface ImagePresenter {

	/**
	 * Converte uma URL de download em {@link VideoImagesDownloadUrlResponse}.
	 *
	 * @param url
	 *            URL de download das imagens do vídeo
	 * @return response contendo a URL
	 */
	VideoImagesDownloadUrlResponse toResponse(String url);

	/**
	 * Converte um objeto {@link VideoUploadUrlOutput} em
	 * {@link VideoUploadUrlResponse}.
	 *
	 * @return {@link VideoUploadUrlResponse} contendo as URLs para upload dos
	 *         vídeos
	 */
	VideoUploadUrlResponse toUploadResponse(VideoUploadUrlOutput output);

	/**
	 * Converte uma lista de objetos {@link VideoUploadUrlOutput} em uma lista de
	 * {@link VideoUploadUrlResponse}.
	 *
	 * @return Lista de {@link VideoUploadUrlResponse} contendo as URLs para upload
	 *         dos vídeos
	 */
	List<VideoUploadUrlResponse> toUploadResponse(List<VideoUploadUrlOutput> outputs);
}
