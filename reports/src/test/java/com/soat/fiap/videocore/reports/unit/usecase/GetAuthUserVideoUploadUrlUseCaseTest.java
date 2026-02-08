package com.soat.fiap.videocore.reports.unit.usecase;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoUploadUrlUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetAuthUserVideoUploadUrlUseCase}.
 */
class GetAuthUserVideoUploadUrlUseCaseTest {

    @Test
    void shouldReturnUploadUrlWhenAllParametersAreValid() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoUploadUrl(anyString(), anyString(), eq("video.mp4"), eq(30L)))
                .thenReturn("http://upload-url");

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act
        String url = useCase.getVideoUploadUrl("video.mp4");

        // assert
        assertEquals("http://upload-url", url);
        verify(videoGateway, times(1))
                .getVideoUploadUrl(eq("user"), anyString(), eq("video.mp4"), eq(30L));
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsNull() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoUploadUrl(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsBlank() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoUploadUrl(" ")
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAuthenticated() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn(" ");

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                NotAuthorizedException.class,
                () -> useCase.getVideoUploadUrl("video.mp4")
        );
    }

    @Test
    void shouldReturnNullWhenGatewayReturnsNull() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoUploadUrl(anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(null);

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act
        String url = useCase.getVideoUploadUrl("video.mp4");

        // assert
        assertNull(url);
    }
}