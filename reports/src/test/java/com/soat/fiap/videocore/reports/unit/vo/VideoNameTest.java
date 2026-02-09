package com.soat.fiap.videocore.reports.unit.vo;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.vo.VideoName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link VideoName}.
 */
class VideoNameTest {

    @Test
    void shouldCreateVideoNameWithValidValue() {
        // Arrange
        String value = "video.mp4";

        // Act
        VideoName name = new VideoName(value);

        // Assert
        assertEquals("video.mp4", name.value());
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsBlank() {
        // Arrange
        String value = " ";

        // Act & Assert
        assertThrows(ReportException.class, () -> new VideoName(value));
    }
}
