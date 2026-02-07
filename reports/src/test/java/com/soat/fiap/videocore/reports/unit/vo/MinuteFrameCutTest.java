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
        // arrange
        long value = 1;

        // act
        MinuteFrameCut cut = new MinuteFrameCut(value);

        // assert
        assertEquals(1, cut.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsZeroOrLess() {
        // arrange
        long value = 0;

        // act + assert
        assertThrows(ReportException.class, () -> new MinuteFrameCut(value));
    }
}
