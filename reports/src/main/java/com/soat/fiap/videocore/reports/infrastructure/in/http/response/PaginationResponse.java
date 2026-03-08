package com.soat.fiap.videocore.reports.infrastructure.in.http.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Entidade de resposta para solicitações paginadas via API HTTP.
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
 *
 * @Note Esta estrutura é implementada como <b>classe</b> e não como
 *       <b>record</b>. Records em Java são implicitamente <code>final</code> e
 *       não podem ser estendidos. Como o <b>Swagger/OpenAPI</b> possui
 *       limitações ao resolver tipos genéricos (<code>T</code>), utilizamos
 *       subclasses concretas para documentação da API. Para viabilizar essa
 *       estratégia, esta estrutura precisa permitir herança, o que não seria
 *       possível caso fosse implementada como <code>record</code>.
 *
 *       Referência:
 *       <a href="https://github.com/springdoc/springdoc-openapi/issues/578">How
 *       to specify a generic type class for Swagger API response</a>
 */
@Getter @RequiredArgsConstructor
@Schema(name = "PaginationResponse", description = "Representa uma resposta paginada contendo metadados de paginação e os dados retornados pela consulta")
public class PaginationResponse<T> {

	@Schema(description = "Número da página atual (baseado em zero)", example = "0")
	private final int page;

	@Schema(description = "Quantidade máxima de elementos por página", example = "10")
	private final int size;

	@Schema(description = "Quantidade total de elementos existentes na consulta", example = "1")
	private final long totalElements;

	@Schema(description = "Quantidade total de páginas disponíveis", example = "1")
	private final int totalPages;

	@Schema(description = "Indica se existe página anterior", example = "false")
	private final boolean hasPrevious;

	@Schema(description = "Indica se existe próxima página", example = "true")
	private final boolean hasNext;

	@Schema(description = "Lista de elementos retornados na página atual")
	private final List<T> content;
}
