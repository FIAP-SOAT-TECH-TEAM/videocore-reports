package com.soat.fiap.videocore.reports.infrastructure.in.http.auth;

import com.soat.fiap.videocore.reports.infrastructure.common.source.AuthenticatedUserSource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Implementação de {@link AuthenticatedUserSource} que extrai as informações do
 * usuário autenticado a partir dos headers HTTP da requisição atual.
 * <p>
 * Os headers são definidos no API Management, após a autenticação do usuário, e
 * propagados para o backend.
 * </p>
 */
@Component
public class HttpAuthenticatedUserSource implements AuthenticatedUserSource {

	private HttpServletRequest getCurrentRequest() {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs instanceof ServletRequestAttributes servletAttrs) {
			return servletAttrs.getRequest();
		}

		throw new IllegalStateException("Nenhuma requisição HTTP ativa encontrada.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSubject() {
		return getCurrentRequest().getHeader("Auth-Subject");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return getCurrentRequest().getHeader("Auth-Name");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEmail() {
		return getCurrentRequest().getHeader("Auth-Email");
	}

}
