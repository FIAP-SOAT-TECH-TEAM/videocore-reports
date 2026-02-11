package com.soat.fiap.videocore.reports.unit.vo;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.vo.ImageMinute;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link ImageMinute}.
 */
class ImageMinuteTest {

    @Test
    void shouldCreateImageMinuteWithZero() {
        // Arrange
        long value = 0;

        // Act
        ImageMinute imageMinute = new ImageMinute(value);

        // Assert
        assertEquals(0, imageMinute.value());
    }

    @Test
    void shouldThrowExceptionWhenImageMinuteIsNegative() {
        // Arrange
        long value = -1;

        // Act & Assert
        assertThrows(ReportException.class, () -> new ImageMinute(value));
    }
}
