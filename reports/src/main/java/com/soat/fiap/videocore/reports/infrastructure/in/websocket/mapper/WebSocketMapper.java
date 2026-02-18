package com.soat.fiap.videocore.reports.infrastructure.in.websocket.mapper;

import org.mapstruct.Mapper;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.in.websocket.payload.ReportPayload;

/**
 * Mapper MapStruct para conversão entre {@link ReportDto} e
 * {@link ReportPayload}.
 */
@Mapper(componentModel = "spring")
public interface WebSocketMapper {

	/**
	 * Converte um {@link ReportDto} para {@link ReportPayload}.
	 *
	 * @param dto
	 *            dados do reporte
	 * @return payload para envio via WebSocket
	 */
	ReportPayload toPayload(ReportDto dto);
}
