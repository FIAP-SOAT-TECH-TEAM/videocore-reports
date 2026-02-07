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
        // arrange
        long value = 0;

        // act
        ImageMinute imageMinute = new ImageMinute(value);

        // assert
        assertEquals(0, imageMinute.value());
    }

    @Test
    void shouldThrowExceptionWhenImageMinuteIsNegative() {
        // arrange
        long value = -1;

        // act + assert
        assertThrows(ReportException.class, () -> new ImageMinute(value));
    }
}
