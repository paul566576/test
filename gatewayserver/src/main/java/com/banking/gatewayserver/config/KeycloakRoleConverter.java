package com.banking.gatewayserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.banking.gatewayserver.constatnts.GatewayConstants.*;


public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>>
{

	@Override
	public Collection<GrantedAuthority> convert(final Jwt source)
	{
		Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get(REALM_ACCESS);
		if (realmAccess == null || realmAccess.isEmpty())
		{
			return new ArrayList<>();
		}

		return ((List<String>) realmAccess.get(ROLES))
				.stream()
				.map(roleName -> ROLE_PREFIX + roleName)
				.map(SimpleGrantedAuthority::new).collect(
						Collectors.toList());
	}
}
