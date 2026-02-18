package com.soat.fiap.videocore.reports.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.reports.core.application.usecase.GetVideoUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ProcessReportException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;

/** Testes unitários do {@link GetVideoUseCase}. */
class GetVideoUseCaseTest {

	@Test
	void shouldReturnVideoWhenFound() {
		// Arrange
		VideoGateway gateway = mock(VideoGateway.class);
		VideoDto dto = mock(VideoDto.class);
		when(gateway.getVideo("url")).thenReturn(dto);

		GetVideoUseCase useCase = new GetVideoUseCase(gateway);

		// Act
		VideoDto result = useCase.getVideo("url");

		// Assert
		assertEquals(dto, result);
	}

	@Test
	void shouldThrowExceptionWhenUrlIsBlank() {
		// Arrange
		VideoGateway gateway = mock(VideoGateway.class);
		GetVideoUseCase useCase = new GetVideoUseCase(gateway);

		// Act & Assert
		assertThrows(ProcessReportException.class, () -> useCase.getVideo(" "));
	}

	@Test
	void shouldThrowExceptionWhenVideoIsNotFound() {
		// Arrange
		VideoGateway gateway = mock(VideoGateway.class);
		when(gateway.getVideo("url")).thenReturn(null);
		GetVideoUseCase useCase = new GetVideoUseCase(gateway);

		// Act & Assert
		assertThrows(ProcessReportException.class, () -> useCase.getVideo("url"));
	}
}
