package com.soat.fiap.videocore.reports.infrastructure.common.source;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;

/**
 * Fonte de dados para operações de persistência de reportes.
 */
public interface ReportDataSource {

    /**
     * Persiste o reporte fornecido e retorna a versão salva.
     *
     * @param reportDto reporte a ser salvo
     * @return reporte persistido
     */
    ReportDto save(ReportDto reportDto);
}