package com.soat.fiap.videocore.reports.unit.controller;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoUploadUrlUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserVideoUploadUrlController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ImagePresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoUploadUrlResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetAuthUserVideoUploadUrlController}.
 */
class GetAuthUserVideoUploadUrlControllerTest {

    @Test
    void shouldReturnUploadUrlResponse() {
        // Arrange
        GetAuthUserVideoUploadUrlUseCase useCase = mock(GetAuthUserVideoUploadUrlUseCase.class);
        ImagePresenter presenter = mock(ImagePresenter.class);

        when(useCase.getVideoUploadUrl("video.mp4")).thenReturn("url");

        var response = mock(VideoUploadUrlResponse.class);
        when(presenter.toUploadResponse("url")).thenReturn(response);

        var controller = new GetAuthUserVideoUploadUrlController(useCase, presenter);

        // Act
        var result = controller.getVideoUploadUrl("video.mp4");

        // Assert
        assertSame(response, result);
        verify(useCase).getVideoUploadUrl("video.mp4");
        verify(presenter).toUploadResponse("url");
    }
}