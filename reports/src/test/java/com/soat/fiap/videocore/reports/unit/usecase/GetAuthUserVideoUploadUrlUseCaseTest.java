package com.soat.fiap.videocore.reports.unit.usecase;

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
        List<String> expectedUrls = List.of(
                "http://upload-url-1",
                "http://upload-url-2"
        );

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoUploadUrl(anyString(), anyString(), eq(videoNames), eq(30L)))
                .thenReturn(expectedUrls);

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act
        List<String> urls = useCase.getVideoUploadUrl(videoNames);

        // assert
        assertEquals(expectedUrls, urls);
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
    void shouldReturnNullWhenGatewayReturnsNull() {
        // arrange
        VideoGateway videoGateway = mock(VideoGateway.class);
        AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

        List<String> videoNames = List.of("video.mp4");

        when(authenticatedUserGateway.getSubject()).thenReturn("user");
        when(videoGateway.getVideoUploadUrl(anyString(), anyString(), eq(videoNames), anyLong()))
                .thenReturn(null);

        GetAuthUserVideoUploadUrlUseCase useCase =
                new GetAuthUserVideoUploadUrlUseCase(videoGateway, authenticatedUserGateway);

        // act
        List<String> urls = useCase.getVideoUploadUrl(videoNames);

        // assert
        assertNull(urls);
    }
}