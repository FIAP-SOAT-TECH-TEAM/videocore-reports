package com.soat.fiap.videocore.reports.unit.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoImagesDownloadUrlUseCase;
import com.soat.fiap.videocore.reports.core.interfaceadapters.controller.GetAuthUserVideoImagesDownloadUrlController;
import com.soat.fiap.videocore.reports.core.interfaceadapters.presenter.ImagePresenter;
import com.soat.fiap.videocore.reports.infrastructure.in.http.response.VideoImagesDownloadUrlResponse;

/** Testes unitários do {@link GetAuthUserVideoImagesDownloadUrlController}. */
class GetAuthUserVideoImagesDownloadUrlControllerTest {

	@Test
	void shouldReturnDownloadUrlResponse() {
		// Arrange
		GetAuthUserVideoImagesDownloadUrlUseCase useCase = mock(GetAuthUserVideoImagesDownloadUrlUseCase.class);
		ImagePresenter presenter = mock(ImagePresenter.class);

		when(useCase.getVideoImagesDownloadUrl("request", "video.mp4")).thenReturn("url");

		var response = mock(VideoImagesDownloadUrlResponse.class);
		when(presenter.toResponse("url")).thenReturn(response);

		var controller = new GetAuthUserVideoImagesDownloadUrlController(useCase, presenter);

		// Act
		var result = controller.getVideoImagesDownloadUrl("request", "video.mp4");

		// Assert
		assertSame(response, result);
		verify(useCase).getVideoImagesDownloadUrl("request", "video.mp4");
		verify(presenter).toResponse("url");
	}
}
