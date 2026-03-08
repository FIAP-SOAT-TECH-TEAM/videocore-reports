package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Slice;

import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.PaginationDTO;
import com.soat.fiap.videocore.reports.core.interfaceadapters.dto.ReportDto;
import com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.entity.ReportEntity;

/** Mapper para conversão entre {@link ReportEntity} e {@link ReportDto}. */
@Mapper(componentModel = "spring")
public interface ReportEntityMapper {

	/** Converte {@link ReportDto} em {@link ReportEntity}. */
	ReportEntity toEntity(ReportDto reportDto);

	/** Converte {@link ReportEntity} em {@link ReportDto}. */
	ReportDto toDto(ReportEntity reportEntity);

	/**
	 * Constrói um {@link PaginationDTO} a partir de um {@link Slice} e de uma lista
	 * de elementos já convertidos.
	 *
	 * @param slice
	 *            fatia retornada pelo repositório contendo os metadados básicos de
	 *            paginação
	 * @param content
	 *            lista de elementos que compõem o conteúdo da página indica o
	 *            limite de quantidade de registros
	 * @param hasNext
	 *            indica a quantidade total de registros (paginação)
	 * @param hasNext
	 *            indica a quantidade total de páginas posteriores (paginação)
	 * @param hasPrevious
	 *            indica se existem elementos anteriores (paginação)
	 * @param hasNext
	 *            indica se existem elementos posteriores (paginação)
	 * @return objeto {@link PaginationDTO} contendo metadados e conteúdo
	 */
	@Mapping(target = "page", expression = "java(slice.getNumber())")
	@Mapping(target = "size", expression = "java(slice.getSize())")
	@Mapping(target = "totalElements", expression = "java(totalElements)")
	@Mapping(target = "totalPages", expression = "java(totalPages)")
	@Mapping(target = "hasPrevious", expression = "java(hasPrevious)")
	@Mapping(target = "hasNext", expression = "java(hasNext)") @Mapping(target = "content", source = "content")
	PaginationDTO<ReportDto> toPaginationDTO(Slice<?> slice, List<ReportDto> content, long totalElements,
			int totalPages, boolean hasPrevious, boolean hasNext);
}
