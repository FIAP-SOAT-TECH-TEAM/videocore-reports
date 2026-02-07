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
        // arrange
        String userId = "user";
        String requestId = "request";

        // act
        Metadata metadata = new Metadata(userId, requestId);

        // assert
        assertEquals(userId, metadata.userId());
        assertEquals(requestId, metadata.requestId());
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        // arrange
        String userId = null;

        // act + assert
        assertThrows(NullPointerException.class, () -> new Metadata(userId, "request"));
    }
}
