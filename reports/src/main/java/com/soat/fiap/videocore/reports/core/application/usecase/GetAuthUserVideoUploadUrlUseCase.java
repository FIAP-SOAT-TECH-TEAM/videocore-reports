package com.soat.fiap.videocore.reports.core.application.usecase;

import com.soat.fiap.videocore.reports.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.core.application.output.VideoUploadUrlOutput;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Caso de uso responsável por obter uma URL de upload de um vídeo
 * para processamento. Para o usuário autenticado.
 */
@Component
@RequiredArgsConstructor
public class GetAuthUserVideoUploadUrlUseCase {

    private final VideoGateway videoGateway;
    private final AuthenticatedUserGateway authenticatedUserGateway;

    /**
     * Obtém uma URL de upload de um vídeo, para o usuário autenticado
     * <p>
     * A URL retornada permite apenas operações de criação e escrita, o caminho do arquivo
     * é construído a partir do {@code userId} e {@code requestId}. Utiliza um tempo de expiração de 30 minutos.
     *
     * @param videoNames Nome do vídeo (arquivo com extensão)
     *
     * @return {@link VideoUploadUrlOutput} contendo as URLs para upload dos vídeos
     */
    @WithSpan(name = "usecase.get.authenticated.user.video.upload.url")
    public List<VideoUploadUrlOutput> getVideoUploadUrl(List<String> videoNames) {
        var userId = authenticatedUserGateway.getSubject();

        CanonicalContext.add("user_id", userId);

        if (userId == null || userId.isBlank())
            throw new NotAuthorizedException("O ID do usuário não pode estar em branco para pesquisa de reportes. Verifique a autenticação.");

        if (videoNames == null || videoNames.isEmpty())
            throw new VideoException("A lista de nome dos vídeos não pode estar vazia para criação da URL de upload.");

        if (videoNames.stream().anyMatch(name -> name == null || name.isBlank()))
            throw new VideoException("A lista de nome dos vídeos não pode conter nomes em branco para criação da URL de upload.");

        if (videoNames.size() > 3)
            throw new VideoException("É permitido o upload de 3 videos por vez");

        if (videoNames.size() != new HashSet<>(videoNames).size())
            throw new VideoException("A lista de nome dos vídeos não pode conter valores repetidos para criação da URL de upload.");

        var expirationMinuteTime = 30L;
        var requestId = UUID.randomUUID().toString();

        var urls = videoGateway.getVideoUploadUrl(userId, requestId, videoNames, expirationMinuteTime);

        return urls.stream().map(url -> new VideoUploadUrlOutput(url, userId, requestId)).toList();
    }
}