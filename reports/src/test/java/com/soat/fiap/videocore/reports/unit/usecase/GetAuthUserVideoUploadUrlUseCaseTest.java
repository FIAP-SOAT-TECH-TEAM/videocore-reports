package com.soat.fiap.videocore.reports.unit.usecase;

import com.soat.fiap.videocore.reports.core.application.output.VideoUploadUrlOutput;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoUploadUrlUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetAuthUserVideoUploadUrlUseCase}.
 */
class GetAuthUserVideoUploadUrlUseCaseTest {

    @Test
    void shouldReturnUploadUrlsWhenAllParametersAreValid() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        List<String> videoNames = List.of("video1.mp4", "video2.mp4");
        List<String> gatewayUrls = List.of(
                "http://upload-url-1",
                "http://upload-url-2"
        );

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoUploadUrl(anyString(), anyString(), eq(videoNames), eq(30L)))
                .thenReturn(gatewayUrls);

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act
        List<VideoUploadUrlOutput> urls = useCase.getVideoUploadUrl(videoNames);

        // assert
        assertEquals(2, urls.size());
        assertEquals("http://upload-url-1", urls.get(0).url());
        assertEquals("http://upload-url-2", urls.get(1).url());
        assertEquals("user", urls.get(0).userId());
        assertEquals("user", urls.get(1).userId());

        verify(videoGateway, times(1))
                .getVideoUploadUrl(eq("user"), anyString(), eq(videoNames), eq(30L));
    }

    @Test
    void shouldThrowExceptionWhenVideoNamesListIsNull() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoUploadUrl(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNamesListIsEmpty() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoUploadUrl(List.of())
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNamesContainsNullOrBlank() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoUploadUrl(List.of("video.mp4", " "))
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNamesSizeIsGreaterThanThree() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                VideoException.class,
                () -> useCase.getVideoUploadUrl(
                        List.of("v1.mp4", "v2.mp4", "v3.mp4", "v4.mp4")
                )
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
                () -> useCase.getVideoUploadUrl(List.of("video.mp4"))
        );
    }

    @Test
    void shouldThrowExceptionWhenGatewayReturnsNull() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        List<String> videoNames = List.of("video.mp4");

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoUploadUrl(anyString(), anyString(), eq(videoNames), anyLong()))
                .thenReturn(null);

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        assertThrows(
                NullPointerException.class,
                () -> useCase.getVideoUploadUrl(videoNames)
        );
    }

    @Test
    void shouldThrowExceptionWhenVideoNamesContainsDuplicatedNames() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        when(authenticatedUserGateway.getSubject()).thenReturn("user");

        List<String> videoNames = List.of("video.mp4", "video.mp4");

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act + assert
        VideoException exception = assertThrows(
                VideoException.class,
                () -> useCase.getVideoUploadUrl(videoNames)
        );

        assertEquals(
                "A lista de nome dos vídeos não pode conter valores repetidos para criação da URL de upload.",
                exception.getMessage()
        );

        verifyNoInteractions(videoGateway);
    }

}