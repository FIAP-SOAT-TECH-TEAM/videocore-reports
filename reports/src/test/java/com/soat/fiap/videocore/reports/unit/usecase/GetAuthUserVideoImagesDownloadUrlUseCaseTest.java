package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetAuthUserVideoImagesDownloadUrlUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoImageDownloadUrlNotFoundException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.AuthenticatedUserGateway;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;

/** Testes unitários do {@link GetAuthUserVideoImagesDownloadUrlUseCase}. */
class GetAuthUserVideoImagesDownloadUrlUseCaseTest {

	@Test
	void shouldReturnDownloadUrlWhenAllParametersAreValid() {
		// Arrange
		VideoGateway videoGateway = mock(VideoGateway.class);
		AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

		when(authenticatedUserGateway.getSubject()).thenReturn("user");
		when(videoGateway.getVideoImagesDownloadUrl("user", "request", "video.mp4", 30L))
				.thenReturn("http://download-url");

		GetAuthUserVideoImagesDownloadUrlUseCase useCase = new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway,
				authenticatedUserGateway);

		// Act
		String url = useCase.getVideoImagesDownloadUrl("request", "video.mp4");

		// Assert
		assertEquals("http://download-url", url);
	}

	@Test
	void shouldThrowExceptionWhenRequestIdIsNull() {
		// Arrange
		VideoGateway videoGateway = mock(VideoGateway.class);
		AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

		GetAuthUserVideoImagesDownloadUrlUseCase useCase = new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway,
				authenticatedUserGateway);

		// Act & Assert
		assertThrows(VideoException.class, () -> useCase.getVideoImagesDownloadUrl(null, "video.mp4"));
	}

	@Test
	void shouldThrowExceptionWhenRequestIdIsBlank() {
		// Arrange
		VideoGateway videoGateway = mock(VideoGateway.class);
		AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

		GetAuthUserVideoImagesDownloadUrlUseCase useCase = new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway,
				authenticatedUserGateway);

		// Act & Assert
		assertThrows(VideoException.class, () -> useCase.getVideoImagesDownloadUrl(" ", "video.mp4"));
	}

	@Test
	void shouldThrowExceptionWhenVideoNameIsNull() {
		// Arrange
		VideoGateway videoGateway = mock(VideoGateway.class);
		AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

		GetAuthUserVideoImagesDownloadUrlUseCase useCase = new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway,
				authenticatedUserGateway);

		// Act & Assert
		assertThrows(VideoException.class, () -> useCase.getVideoImagesDownloadUrl("request", null));
	}

	@Test
	void shouldThrowExceptionWhenVideoNameIsBlank() {
		// Arrange
		VideoGateway videoGateway = mock(VideoGateway.class);
		AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

		GetAuthUserVideoImagesDownloadUrlUseCase useCase = new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway,
				authenticatedUserGateway);

		// Act & Assert
		assertThrows(VideoException.class, () -> useCase.getVideoImagesDownloadUrl("request", " "));
	}

	@Test
	void shouldThrowExceptionWhenUserIsNotAuthenticated() {
		// Arrange
		VideoGateway videoGateway = mock(VideoGateway.class);
		AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

		when(authenticatedUserGateway.getSubject()).thenReturn(" ");

		GetAuthUserVideoImagesDownloadUrlUseCase useCase = new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway,
				authenticatedUserGateway);

		// Act & Assert
		assertThrows(NotAuthorizedException.class, () -> useCase.getVideoImagesDownloadUrl("request", "video.mp4"));
	}

	@Test
	void shouldThrowExceptionWhenDownloadUrlIsNull() {
		// Arrange
		VideoGateway videoGateway = mock(VideoGateway.class);
		AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

		when(authenticatedUserGateway.getSubject()).thenReturn("user");
		when(videoGateway.getVideoImagesDownloadUrl("user", "request", "video.mp4", 30L)).thenReturn(null);

		GetAuthUserVideoImagesDownloadUrlUseCase useCase = new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway,
				authenticatedUserGateway);

		// Act & Assert
		assertThrows(VideoImageDownloadUrlNotFoundException.class,
				() -> useCase.getVideoImagesDownloadUrl("request", "video.mp4"));
	}

	@Test
	void shouldThrowExceptionWhenDownloadUrlIsBlank() {
		// Arrange
		VideoGateway videoGateway = mock(VideoGateway.class);
		AuthenticatedUserGateway authenticatedUserGateway = mock(AuthenticatedUserGateway.class);

		when(authenticatedUserGateway.getSubject()).thenReturn("user");
		when(videoGateway.getVideoImagesDownloadUrl("user", "request", "video.mp4", 30L)).thenReturn(" ");

		GetAuthUserVideoImagesDownloadUrlUseCase useCase = new GetAuthUserVideoImagesDownloadUrlUseCase(videoGateway,
				authenticatedUserGateway);

		// Act & Assert
		assertThrows(VideoImageDownloadUrlNotFoundException.class,
				() -> useCase.getVideoImagesDownloadUrl("request", "video.mp4"));
	}
}
