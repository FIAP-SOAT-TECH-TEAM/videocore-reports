package com.soat.fiap.videocore.reports.infrastructure.out.persistence.cosmosdb.nosql.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

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
	 * Constrói um {@link PaginationDTO} a partir de um {@link Page} e de uma lista
	 * de elementos já convertidos.
	 *
	 * @param page
	 *            objeto contendo os metadadaos de paginação
	 * @param content
	 *            lista de elementos que compõem o conteúdo da página
	 *
	 * @return objeto {@link PaginationDTO} contendo metadados e conteúdo
	 */
	@Mapping(target = "page", expression = "java(page.getNumber())")
	@Mapping(target = "size", expression = "java(page.getSize())")
	@Mapping(target = "totalElements", expression = "java(page.getTotalElements())")
	@Mapping(target = "totalPages", expression = "java(page.getTotalPages())")
	@Mapping(target = "hasPrevious", expression = "java(page.hasPrevious())")
	@Mapping(target = "hasNext", expression = "java(page.hasNext())") @Mapping(target = "content", source = "content")
	PaginationDTO<ReportDto> toPaginationDTO(Page<?> page, List<ReportDto> content);
}
