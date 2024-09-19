package com.banking.secured_banking_app.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class KeycloakOpaqueRoleConverter implements OpaqueTokenAuthenticationConverter
{

	/**
	 * @param introspectedToken      the bearer token used to perform token introspection
	 * @param authenticatedPrincipal the result of token introspection
	 * @return
	 */
	@Override
	public Authentication convert(final String introspectedToken, final OAuth2AuthenticatedPrincipal authenticatedPrincipal)
	{
		final String userName = authenticatedPrincipal.getAttribute("preferred_username");
		final Map<String, Object> reaslAccess = authenticatedPrincipal.getAttribute("realm_access");
		final List<GrantedAuthority> roles = ((List<String>) reaslAccess.get("roles"))
				.stream().map(roleName -> "ROLE_" + roleName)
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(userName, null, roles);
	}
}
