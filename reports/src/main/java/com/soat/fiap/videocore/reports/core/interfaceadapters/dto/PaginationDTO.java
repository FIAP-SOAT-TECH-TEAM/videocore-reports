package com.soat.fiap.videocore.reports.core.interfaceadapters.dto;

import java.util.List;

/**
 * DTO genérico utilizado para representar resultados paginados.
 *
 * <p>
 * Contém metadados de paginação juntamente com os dados efetivos retornados
 * pela consulta. Este objeto é utilizado para transportar resultados de
 * consultas paginadas entre as camadas da aplicação.
 *
 * <p>
 * Os metadados permitem que consumidores da API saibam:
 * <ul>
 * <li>qual página foi retornada</li>
 * <li>quantos elementos existem no total</li>
 * <li>quantas páginas existem</li>
 * <li>quantos elementos existem na página atual</li>
 * <li>se existe página anterior</li>
 * <li>se existe próxima página</li>
 * </ul>
 *
 * @param <T>
 *            tipo dos elementos retornados
 * @param page
 *            número da página atual (baseado em zero)
 * @param size
 *            quantidade de elementos por página
 * @param totalElements
 *            quantidade total de elementos existentes
 * @param totalPages
 *            quantidade total de páginas disponíveis
 * @param hasPrevious
 *            indica se existe página anterior
 * @param hasNext
 *            indica se existe próxima página
 * @param content
 *            lista de elementos retornados na página atual
 */
public record PaginationDTO<T>(int page, int size, long totalElements, int totalPages, boolean hasPrevious,
		boolean hasNext, List<T> content) {
}
