package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoImageDownloadUrlNotFoundException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por recuperar a URL de download das imagens
 * capturadas de um vídeo enviadas pelo usuário autenticado.
 */
@Component
@RequiredArgsConstructor
public class GetAuthUserVideoImagesDownloadUrlUseCase {

    private final VideoGateway videoGateway;
    private final AuthenticatedUserGateway authenticatedUserGateway;

    /**
     * Recupera a URL de download das imagens de um vídeo, enviado pelo um usuário autenticado,
     * a partir do id da requisição e do nome do vídeo. Utiliza um tempo de expiração de 30 minutos.
     *
     * @param requestId identificador da requisição
     * @param videoName nome do vídeo
     *
     * @return URL para download das imagens do vídeo
     */
    @WithSpan(name = "usecase.get.authenticated.user.video.images.download.url")
    public String getVideoImagesDownloadUrl(String requestId, String videoName) {
        CanonicalContext.add("request_id", requestId);
        CanonicalContext.add("video_name", videoName);
        
        if (requestId == null || requestId.isBlank()) {
            throw new VideoException("requestId não pode ser nulo ou vazio para pesquisa de URL de download das imagens");
        }
        if (videoName == null || videoName.isBlank()) {
            throw new VideoException("videoName não pode ser nulo ou vazio para pesquisa de URL de download das imagens");
        }

        var userId = authenticatedUserGateway.getSubject();

        CanonicalContext.add("user_id", userId);

        if (userId == null || userId.isBlank())
            throw new NotAuthorizedException("O ID do usuário não pode estar em branco para obtenção da URL de download do arquivo de imagens. Verifique a autenticação.");

        var expirationMinuteTime = 30L;

        CanonicalContext.add("expiration_minute_time", expirationMinuteTime);

        var downloadUrl = videoGateway.getVideoImagesDownloadUrl(userId, requestId, videoName, expirationMinuteTime);

        CanonicalContext.add("download_url", downloadUrl);

        if (downloadUrl == null || downloadUrl.isBlank())
            throw new VideoImageDownloadUrlNotFoundException("URL de download do arquivo de imagens do vídeo informado não encontrada. RequestID: %s e VideoName: %s", requestId, videoName);

        return downloadUrl;
    }
}