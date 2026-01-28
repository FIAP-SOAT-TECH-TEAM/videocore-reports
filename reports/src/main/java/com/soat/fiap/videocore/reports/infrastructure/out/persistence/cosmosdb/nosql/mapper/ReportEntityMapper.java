package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity.ReportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversão entre {@link ReportEntity} e {@link ReportDto}.
 */
@Mapper(componentModel = "spring")
public interface ReportEntityMapper {

    /**
     * Converte {@link ReportDto} em {@link ReportEntity}.
     */
    @Mapping(target = "userId", expression = "java(reportDto.userId() != null ? reportDto.userId().toString() : null)")
    @Mapping(target = "requestId", expression = "java(reportDto.requestId() != null ? reportDto.requestId().toString() : null)")
    ReportEntity toEntity(ReportDto reportDto);

    /**
     * Converte {@link ReportEntity} em {@link ReportDto}.
     */
    @Mapping(target = "userId", expression = "java(reportEntity.getUserId() != null ? java.util.UUID.fromString(reportEntity.getUserId()) : null)")
    @Mapping(target = "requestId", expression = "java(reportEntity.getRequestId() != null ? java.util.UUID.fromString(reportEntity.getRequestId()) : null)")
    ReportDto toDto(ReportEntity reportEntity);
}