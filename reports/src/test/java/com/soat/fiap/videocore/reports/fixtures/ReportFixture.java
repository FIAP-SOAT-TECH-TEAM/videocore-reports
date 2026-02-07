package com.soat.fiap.videocore.reports.fixtures;

import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.*;

import java.time.Instant;

/**
 * Fixture para criação de objetos {@link Report} válidos para testes.
 * Centraliza a construção de dependências reutilizáveis.
 */
public final class ReportFixture {

    private ReportFixture() {
    }

    /**
     * Cria um {@link Report} totalmente válido.
     *
     * @return report válido
     */
    public static Report validReport() {
        return new Report(
                videoName(),
                imageMinute(),
                minuteFrameCut(),
                metadata(),
                percentStatusProcess(),
                Instant.now(),
                ProcessStatus.PROCESSING
        );
    }

    /**
     * @return {@link VideoName} válido
     */
    public static VideoName videoName() {
        return new VideoName("video.mp4");
    }

    /**
     * @return {@link ImageMinute} válido
     */
    public static ImageMinute imageMinute() {
        return new ImageMinute(1);
    }

    /**
     * @return {@link MinuteFrameCut} válido
     */
    public static MinuteFrameCut minuteFrameCut() {
        return new MinuteFrameCut(1);
    }

    /**
     * @return {@link Metadata} válido
     */
    public static Metadata metadata() {
        return new Metadata("user-1", "request-1");
    }

    /**
     * @return {@link PercentStatusProcess} válido
     */
    public static PercentStatusProcess percentStatusProcess() {
        return new PercentStatusProcess(10.0);
    }
}
