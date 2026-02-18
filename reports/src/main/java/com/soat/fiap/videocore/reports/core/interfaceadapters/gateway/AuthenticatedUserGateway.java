package com.soat.fiap.videocore.reports.core.interfaceadapters.gateway;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.reports.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.reports.infrastructure.common.source.AuthenticatedUserSource;

/**
 * Gateway para acessar informações do usuário autenticado no sistema.
 *
 * <p>
 * Cada método expõe um atributo específico do usuário obtido via
 * {@link AuthenticatedUserSource}.
 */
@Component
public class AuthenticatedUserGateway {

	private final AuthenticatedUserSource authenticatedUserSource;

	/**
	 * Construtor do gateway.
	 *
	 * @param authenticatedUserSource
	 *            fonte de dados do usuário autenticado
	 */
	public AuthenticatedUserGateway(AuthenticatedUserSource authenticatedUserSource) {
		this.authenticatedUserSource = authenticatedUserSource;
	}

	/**
	 * Retorna o identificador único (sub) do usuário autenticado.
	 *
	 * @return sub do usuário
	 */
	@WithSpan(name = "gateway.get.auth.subject")
	public String getSubject() {
		return authenticatedUserSource.getSubject();
	}

	/**
	 * Retorna o nome completo do usuário autenticado.
	 *
	 * @return nome do usuário
	 */
	@WithSpan(name = "gateway.get.auth.name")
	public String getName() {
		return authenticatedUserSource.getName();
	}

	/**
	 * Retorna o email do usuário autenticado.
	 *
	 * @return email do usuário
	 */
	@WithSpan(name = "gateway.get.auth.email")
	public String getEmail() {
		return authenticatedUserSource.getEmail();
	}
}
