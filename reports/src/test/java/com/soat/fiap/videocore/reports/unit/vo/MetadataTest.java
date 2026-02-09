package com.soat.fiap.videocore.reports.unit.vo;

import com.soat.fiap.videocore.reports.core.domain.vo.Metadata;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link Metadata}.
 */
class MetadataTest {

    @Test
    void shouldCreateMetadataWithValidValues() {
        // Arrange
        String userId = "user";
        String requestId = "request";
        String traceId = "trace";

        // Act
        Metadata metadata = new Metadata(userId, requestId, traceId);

        // Assert
        assertEquals(userId, metadata.userId());
        assertEquals(requestId, metadata.requestId());
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        String userId = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Metadata(userId, "request", "trace"));
    }
}
