package com.soat.fiap.videocore.reports.unit.usecase;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoImagesDownloadUrlUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoImageDownloadUrlNotFoundException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetAuthUserVideoImagesDownloadUrlUseCase}.
 */
class GetAuthUserVideoImagesDownloadUrlUseCaseTest {

    @Test
    void shouldReturnDownloadUrlWhenAllParametersAreValid() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoImagesDownloadUrl("user", "request", "video.mp4", 30L))
                .thenReturn("http://download-url");

        GetAuthUserVideoImagesDownloadUrlUseCase useCase =
                new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act
        String url = useCase.getVideoImagesDownloadUrl("request", "video.mp4");

        // assert
        assertEquals("http://download-url", url);
    }

    @Test
    void shouldThrowExceptionWhenRequestIdIsNull() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        GetAuthUserVideoImagesDownloadUrlUseCase useCase =
                new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoImagesDownloadUrl(null, "video.mp4")
        );
    }

    @Test
    void shouldThrowExceptionWhenRequestIdIsBlank() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        GetAuthUserVideoImagesDownloadUrlUseCase useCase =
                new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoImagesDownloadUrl(" ", "video.mp4")
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsNull() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        GetAuthUserVideoImagesDownloadUrlUseCase useCase =
                new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoImagesDownloadUrl("request", null)
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsBlank() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        GetAuthUserVideoImagesDownloadUrlUseCase useCase =
                new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoImagesDownloadUrl("request", " ")
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAuthenticated() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn(" ");

        GetAuthUserVideoImagesDownloadUrlUseCase useCase =
                new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                NotAuthorizedException.class,
                () -> useCase.getVideoImagesDownloadUrl("request", "video.mp4")
        );
    }

    @Test
    void shouldThrowExceptionWhenDownloadUrlIsNull() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoImagesDownloadUrl("user", "request", "video.mp4", 30L))
                .thenReturn(null);

        GetAuthUserVideoImagesDownloadUrlUseCase useCase =
                new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoImageDownloadUrlNotFoundException.class,
                () -> useCase.getVideoImagesDownloadUrl("request", "video.mp4")
        );
    }

    @Test
    void shouldThrowExceptionWhenDownloadUrlIsBlank() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoImagesDownloadUrl("user", "request", "video.mp4", 30L))
                .thenReturn(" ");

        GetAuthUserVideoImagesDownloadUrlUseCase useCase =
                new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoImageDownloadUrlNotFoundException.class,
                () -> useCase.getVideoImagesDownloadUrl("request", "video.mp4")
        );
    }
}