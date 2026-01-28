package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidade de infraestrutura que representa um reporte de atualização do status de processamento de um vídeo.
 */
@Data
@Container(containerName = "report")
public class ReportEntity {
    @Id
    @GeneratedValue
    private String id;

    private String videoName;

    @PartitionKey
    private String userId;

    private String requestId;

    private long durationMinutes;

    private long frameCutMinutes;

    private Double percentStatusProcess;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant reportTime;

    private ProcessStatus status;
}