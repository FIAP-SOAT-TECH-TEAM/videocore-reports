package com.soat.fiap.videocore.reports.core.interfaceadapters.dto;

import com.soat.fiap.videocore.reports.core.domain.vo.ProcessStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO utilizado para representar dados da entidade Report. Serve como objeto
 * de transferência entre o domínio e o mundo externo (DataSource).
 * @param id                    Identificador único do reporte. Gerado por fontes externas
 * @param videoName             Nome do vídeo.
 * @param userId                Identificador do usuário dono do vídeo.
 * @param requestId             Identificador da requisição de processamento.
 * @param imageMinute           Minuto em que a imagem foi capturada
 * @param frameCutMinutes       Intervalo de corte de frames em minutos.
 * @param percentStatusProcess  Percentual do vídeo já processado.
 * @param reportTime            Momento em que o reporte foi realizado.
 * @param status                Status de processamento.
 */
public record ReportDto(
        String id,
        String videoName,
        UUID userId,
        UUID requestId,
        long imageMinute,
        long frameCutMinutes,
        Double percentStatusProcess,
        Instant reportTime,
        ProcessStatus status
) {

}