package com.soat.fiap.videocore.reports.unit.vo;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.vo.PercentStatusProcess;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link PercentStatusProcess}.
 */
class PercentStatusProcessTest {

    @Test
    void shouldCreatePercentStatusProcessWithValidValue() {
        // Arrange
        Double value = 50.0;

        // Act
        PercentStatusProcess percent = new PercentStatusProcess(value);

        // Assert
        assertEquals(50.0, percent.value());
    }

    @Test
    void shouldThrowExceptionWhenPercentIsGreaterThan100() {
        // Arrange
        Double value = 101.0;

        // Act & Assert
        assertThrows(ReportException.class, () -> new PercentStatusProcess(value));
    }
}
