package com.soat.fiap.videocore.reports.core.domain.model;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.vo.*;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Objeto de domínio que representa um reporte de atualização no status de processamento de um vídeo.
 */
@Getter
public class Report {
    /**
     * Identificador único do reporte. Gerado por fontes externas.
     */
    private final String id;

    /**
     * Nome do vídeo.
     */
    private final VideoName videoName;

    /**
     * Duração total do vídeo em minutos.
     */
    private final DurationMinutes durationMinutes;

    /**
     * Tempo de corte dos frames para captura de imagens.
     */
    private final MinuteFrameCut minuteFrameCut;

    /**
     * Metadados de rastreabilidade do processamento.
     */
    private final Metadata metadata;

    /**
     * Percentual de progresso no processamento do vídeo.
     */
    private final PercentStatusProcess percentStatusProcess;

    /**
     * Momento em que o reporte foi realizado.
     */
    private final Instant reportTime;

    /**
     * Status de processamento.
     */
    private final ProcessStatus status;

    public Report(String id, VideoName videoName, DurationMinutes durationMinutes, MinuteFrameCut minuteFrameCut,
                  Metadata metadata, PercentStatusProcess percentStatusProcess, Instant reportTime, ProcessStatus status) {
        this.id = id;
        this.videoName = videoName;
        this.durationMinutes = durationMinutes;
        this.minuteFrameCut = minuteFrameCut;
        this.metadata = metadata;
        this.percentStatusProcess = percentStatusProcess;
        this.reportTime = reportTime;
        this.status = status;

        validate();
    }

    private void validate() {
        Objects.requireNonNull(videoName, "videoName não pode ser nulo");
        Objects.requireNonNull(durationMinutes, "durationMinutes não pode ser nulo");
        Objects.requireNonNull(minuteFrameCut, "minuteFrameCut não pode ser nulo");
        Objects.requireNonNull(metadata, "metadata não pode ser nulo");
        Objects.requireNonNull(percentStatusProcess, "percentStatusProcess não pode ser nulo");
        Objects.requireNonNull(reportTime, "reportTime não pode ser nulo");
        Objects.requireNonNull(status, "status não pode ser nulo");
    }

    /**
     * Retorna o nome do vídeo como valor primitivo.
     *
     * @return nome do vídeo
     */
    public String getVideoName() {
        return videoName.value();
    }

    /**
     * Retorna a duração total do vídeo em minutos.
     *
     * @return duração em minutos
     */
    public long getDurationMinutes() {
        return durationMinutes.value();
    }

    /**
     * Retorna o intervalo de captura de frames em minutos.
     *
     * @return intervalo de captura em minutos
     */
    public long getMinuteFrameCut() {
        return minuteFrameCut.value();
    }

    /**
     * Retorna o identificador do usuário associado ao processamento.
     *
     * @return userId
     */
    public UUID getUserId() {
        return metadata.userId();
    }

    /**
     * Retorna o identificador da requisição associada ao processamento.
     *
     * @return requestId
     */
    public UUID getRequestId() {
        return metadata.requestId();
    }

    /**
     * Retorna o percentual de progresso do processamento do vídeo.
     *
     * @return o percentual de progresso do processamento do vídeo
     */
    public Double getPercentStatusProcess() {
        return percentStatusProcess.value();
    }
}