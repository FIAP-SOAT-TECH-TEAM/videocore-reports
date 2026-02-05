package com.soat.fiap.videocore.reports.core.domain.exceptions;

/**
 * Exceção lançada quando a url de download do arquivo de imagens de um vídeo processado for nula ou vazia
 */
public class VideoImageDownloadUrlNotFound extends RuntimeException {

	public VideoImageDownloadUrlNotFound(String message) {
		super(message);
	}

	public VideoImageDownloadUrlNotFound(String message, Throwable cause) {
		super(message, cause);
	}

    public VideoImageDownloadUrlNotFound(String message, Object... args) {
        super(String.format(message, args));
    }
}