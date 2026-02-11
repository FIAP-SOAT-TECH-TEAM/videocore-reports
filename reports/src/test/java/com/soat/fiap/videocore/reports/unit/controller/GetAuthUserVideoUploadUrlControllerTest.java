package com.soat.fiap.videocore.reports.unit.controller;

import com.soat.fiap.videocore.reports.core.application.output.VideoUploadUrlOutput;
import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoUploadUrlUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserVideoUploadUrlController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ImagePresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoUploadUrlResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetAuthUserVideoUploadUrlController}.
 */
class GetAuthUserVideoUploadUrlControllerTest {

    @Test
    void shouldReturnUploadUrlResponseList() {
        // Arrange
        GetAuthUserVideoUploadUrlUseCase useCase = mock(GetAuthUserVideoUploadUrlUseCase.class);
        ImagePresenter presenter = mock(ImagePresenter.class);

        List<String> videoNames = List.of("video.mp4");
        List<VideoUploadUrlOutput> outputs = List.of(
                new VideoUploadUrlOutput("url", "user", "request")
        );

        when(useCase.getVideoUploadUrl(videoNames)).thenReturn(outputs);

        List<VideoUploadUrlResponse> responseList = List.of(mock(VideoUploadUrlResponse.class));
        when(presenter.toUploadResponse(outputs)).thenReturn(responseList);

        var controller = new GetAuthUserVideoUploadUrlController(useCase, presenter);

        // Act
        var result = controller.getVideoUploadUrl(videoNames);

        // Assert
        assertSame(responseList, result);
        verify(useCase).getVideoUploadUrl(videoNames);
        verify(presenter).toUploadResponse(outputs);
    }
}