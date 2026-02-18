package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper;

import org.mapstruct.Mapper;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity.ReportEntity;

/** Mapper para conversão entre {@link ReportEntity} e {@link ReportDto}. */
@Mapper(componentModel = "spring")
public interface ReportEntityMapper {

	/** Converte {@link ReportDto} em {@link ReportEntity}. */
	ReportEntity toEntity(ReportDto reportDto);

	/** Converte {@link ReportEntity} em {@link ReportDto}. */
	ReportDto toDto(ReportEntity reportEntity);
}
