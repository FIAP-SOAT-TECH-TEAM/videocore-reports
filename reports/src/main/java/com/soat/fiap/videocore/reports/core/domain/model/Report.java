package com.soat.fiap.videocore.reports.core.domain.model;

import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.vo.*;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * Objeto de domínio que representa um reporte de atualização no status de processamento de um vídeo.
 */
@Getter
public class Report {
    /**
     * Identificador único do reporte. Gerado por fontes externas.
     */
    private String id;

    /**
     * Nome do vídeo.
     */
    private VideoName videoName;

    /**
     * Minuto em que a imagem foi capturada.
     */
    private ImageMinute imageMinute;

    /**
     * Tempo de corte dos frames para captura de imagens.
     */
    private MinuteFrameCut minuteFrameCut;

    /**
     * Metadados de rastreabilidade do processamento.
     */
    private Metadata metadata;

    /**
     * Percentual de progresso no processamento do vídeo.
     */
    private PercentStatusProcess percentStatusProcess;

    /**
     * Momento em que o reporte foi realizado.
     */
    private Instant reportTime;

    /**
     * Status de processamento.
     */
    private ProcessStatus status;

    public Report(VideoName videoName, ImageMinute imageMinute, MinuteFrameCut minuteFrameCut,
                  Metadata metadata, PercentStatusProcess percentStatusProcess, Instant reportTime, ProcessStatus status) {
        this.videoName = videoName;
        this.imageMinute = imageMinute;
        this.minuteFrameCut = minuteFrameCut;
        this.metadata = metadata;
        this.percentStatusProcess = percentStatusProcess;
        this.reportTime = reportTime;
        this.status = status;

        validate();
    }

    private void validate() {
        Objects.requireNonNull(videoName, "videoName não pode ser nulo");
        Objects.requireNonNull(imageMinute, "imageMinute não pode ser nulo");
        Objects.requireNonNull(minuteFrameCut, "minuteFrameCut não pode ser nulo");
        Objects.requireNonNull(metadata, "metadata não pode ser nulo");
        Objects.requireNonNull(percentStatusProcess, "percentStatusProcess não pode ser nulo");
        Objects.requireNonNull(reportTime, "reportTime não pode ser nulo");
        Objects.requireNonNull(status, "status não pode ser nulo");
    }

    /**
     * Define o identificador único do reporte.
     *
     * @param id identificador único do reporte
     * @throws NullPointerException se {@code id} for {@code null}
     * @throws ReportException se {@code id} estiver em branco
     */
    public void setId(String id) {
        Objects.requireNonNull(id, "id não pode ser nulo");

        if (id.isBlank()) {
            throw new ReportException("id não pode ser branco");
        }

        this.id = id;
    }

    /**
     * Define o nome do vídeo.
     *
     * @param videoName nome do vídeo
     * @throws NullPointerException se {@code videoName} for {@code null}
     */
    public void setVideoName(VideoName videoName) {
        this.videoName = Objects.requireNonNull(videoName, "videoName não pode ser nulo");
    }

    /**
     * Define o minuto em que uma imagem foi capturada.
     *
     * @param imageMinute duração total em minutos
     * @throws NullPointerException se {@code imageMinute} for {@code null}
     */
    public void setImageMinute(ImageMinute imageMinute) {
        this.imageMinute = Objects.requireNonNull(imageMinute, "imageMinute não pode ser nulo");
    }

    /**
     * Define o tempo de corte dos frames para captura de imagens.
     *
     * @param minuteFrameCut intervalo de captura em minutos
     * @throws NullPointerException se {@code minuteFrameCut} for {@code null}
     */
    public void setMinuteFrameCut(MinuteFrameCut minuteFrameCut) {
        this.minuteFrameCut = Objects.requireNonNull(minuteFrameCut, "minuteFrameCut não pode ser nulo");
    }

    /**
     * Define os metadados de rastreabilidade do processamento.
     *
     * @param metadata metadados de rastreabilidade
     * @throws NullPointerException se {@code metadata} for {@code null}
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = Objects.requireNonNull(metadata, "metadata não pode ser nulo");
    }

    /**
     * Define o percentual de progresso do processamento do vídeo.
     *
     * @param percentStatusProcess percentual de progresso do processamento
     * @throws NullPointerException se {@code percentStatusProcess} for {@code null}
     */
    public void setPercentStatusProcess(PercentStatusProcess percentStatusProcess) {
        this.percentStatusProcess = Objects.requireNonNull(percentStatusProcess, "percentStatusProcess não pode ser nulo");
    }

    /**
     * Define o momento em que o reporte foi realizado.
     *
     * @param reportTime instante do reporte
     * @throws NullPointerException se {@code reportTime} for {@code null}
     */
    public void setReportTime(Instant reportTime) {
        this.reportTime = Objects.requireNonNull(reportTime, "reportTime não pode ser nulo");
    }

    /**
     * Define o status de processamento.
     *
     * @param status status do processamento
     * @throws NullPointerException se {@code status} for {@code null}
     */
    public void setStatus(ProcessStatus status) {
        this.status = Objects.requireNonNull(status, "status não pode ser nulo");
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
     * Retorna o minuto em que a imagem foi capturada.
     *
     * @return o minuto em que a imagem foi capturada
     */
    public long getImageMinute() {
        return imageMinute.value();
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
    public String getUserId() {
        return metadata.userId();
    }

    /**
     * Retorna o identificador da requisição associada ao processamento.
     *
     * @return requestId
     */
    public String getRequestId() {
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