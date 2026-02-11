package com.soat.fiap.videocore.reports.unit.vo;


import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.vo.MinuteFrameCut;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link MinuteFrameCut}.
 */
class MinuteFrameCutTest {

    @Test
    void shouldCreateMinuteFrameCutWithValidValue() {
        // Arrange
        long value = 1;

        // Act
        MinuteFrameCut cut = new MinuteFrameCut(value);

        // Assert
        assertEquals(1, cut.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsZeroOrLess() {
        // Arrange
        long value = 0;

        // Act & Assert
        assertThrows(ReportException.class, () -> new MinuteFrameCut(value));
    }
}
