package com.soat.fiap.videocore.reports.unit.usecase;

import com.soat.fiap.videocore.reports.core.application.usecase.GetVideoUseCase;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ProcessReportException;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.reports.core.interfaceadapters.gateway.VideoGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetVideoUseCase}.
 */
class GetVideoUseCaseTest {

    @Test
    void shouldReturnVideoWhenFound() {
        // arrange
        VideoGateway gateway = mock(VideoGateway.class);
        VideoDto dto = mock(VideoDto.class);
        when(gateway.getVideo("url")).thenReturn(dto);

        GetVideoUseCase useCase = new GetVideoUseCase(gateway);

        // act
        VideoDto result = useCase.getVideo("url");

        // assert
        assertEquals(dto, result);
    }

    @Test
    void shouldThrowExceptionWhenUrlIsBlank() {
        // arrange
        VideoGateway gateway = mock(VideoGateway.class);
        GetVideoUseCase useCase = new GetVideoUseCase(gateway);

        // act + assert
        assertThrows(ProcessReportException.class, () -> useCase.getVideo(" "));
    }

    @Test
    void shouldThrowExceptionWhenVideoIsNotFound() {
        // arrange
        VideoGateway gateway = mock(VideoGateway.class);
        when(gateway.getVideo("url")).thenReturn(null);
        GetVideoUseCase useCase = new GetVideoUseCase(gateway);

        // act + assert
        assertThrows(ProcessReportException.class, () -> useCase.getVideo("url"));
    }
}
