package com.soat.fiap.videocore.reports.core.interfaceadapters.mapper;

import com.soat.fiap.videocore.reports.core.domain.model.Report;
import com.soat.fiap.videocore.reports.core.domain.vo.*;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper responsável por converter entre objetos de domínio {@link Report}
 * e DTOs {@link ReportDto}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper {

    /**
     * Converte um objeto de domínio {@link Report} em um DTO {@link ReportDto}.
     *
     * @param report objeto de domínio a ser convertido
     * @return DTO correspondente com os valores mapeados
     */
    @Mapping(target = "videoName", expression = "java(report.getVideoName())")
    @Mapping(target = "userId", expression = "java(report.getUserId())")
    @Mapping(target = "requestId", expression = "java(report.getRequestId())")
    @Mapping(target = "durationMinutes", expression = "java(report.getDurationMinutes())")
    @Mapping(target = "frameCutMinutes", expression = "java(report.getMinuteFrameCut())")
    @Mapping(target = "percentStatusProcess", expression = "java(report.getPercentStatusProcess())")
    ReportDto toDto(Report report);

    /**
     * Converte um DTO {@link ReportDto} em objeto de domínio {@link Report}.
     *
     * @param dto DTO a ser convertido
     * @return objeto de domínio correspondente com os valores mapeados
     */
    @Mapping(target = "videoName", expression = "java(new VideoName(dto.videoName()))")
    @Mapping(target = "durationMinutes", expression = "java(new DurationMinutes(dto.durationMinutes()))")
    @Mapping(target = "minuteFrameCut", expression = "java(new MinuteFrameCut(dto.frameCutMinutes()))")
    @Mapping(target = "metadata", expression = "java(new Metadata(dto.userId(), dto.requestId()))")
    @Mapping(target = "percentStatusProcess", expression = "java(new PercentStatusProcess(dto.percentStatusProcess()))")
    Report toModel(ReportDto dto);
}