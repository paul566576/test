package com.authserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
public class BankingSecurityConfig
{

	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(final HttpSecurity http)
			throws Exception
	{
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.oidc(Customizer.withDefaults());   // Enable OpenID Connect 1.0
		http
				// Redirect to the login page when not authenticated from the
				// authorization endpoint
				.exceptionHandling((exceptions) -> exceptions
						.defaultAuthenticationEntryPointFor(
								new LoginUrlAuthenticationEntryPoint("/login"),
								new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
						)
				)
				// Accept access tokens for User Info and/or Client Registration
				.oauth2ResourceServer((resourceServer) -> resourceServer
						.jwt(Customizer.withDefaults()));

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http)
			throws Exception
	{
		http
				.authorizeHttpRequests((authorize) -> authorize
						.anyRequest().authenticated()
				)
				// Form login handles the redirect to the login page from the
				// authorization server filter chain
				.formLogin(Customizer.withDefaults());

		return http.build();
	}


	@Bean
	public RegisteredClientRepository registeredClientRepository()
	{
		final RegisteredClient clientCredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("bankingapi")
				.clientSecret("{noop}$2a$12$V5IyM7BELoQ6j2lWrZX8MuenXKi3hF747TKDhJYqh9HAoQ2xI2DkW")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.scopes(scopeConfig -> scopeConfig.addAll(List.of(OidcScopes.OPENID, "ADMIN", "USER")))
				.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
				.build();

		final RegisteredClient authCodeClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("bankingclient")
				.clientSecret("{noop}$2a$12$0Y/ks6xvQCvHzjlz1PMrWuNL5lXIJA4cXpvG23lofiT4mceNE8vOS")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("https://oauth.pstmn.io/v1/callback")

				.scopes(scopeConfig -> scopeConfig.addAll(List.of(OidcScopes.OPENID, OidcScopes.EMAIL)))
				.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
						.refreshTokenTimeToLive(Duration.ofHours(8)).reuseRefreshTokens(false)
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
				.build();

		final RegisteredClient pkceOauthClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("bankingpublicclient")
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("https://oauth.pstmn.io/v1/callback")
				.scopes(scopeConfig -> scopeConfig.addAll(List.of(OidcScopes.OPENID, OidcScopes.EMAIL)))
				.clientSettings(ClientSettings.builder().requireProofKey(true).build())
				.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
						.refreshTokenTimeToLive(Duration.ofHours(8)).reuseRefreshTokens(false)
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
				.build();

		return new InMemoryRegisteredClientRepository(clientCredClient, authCodeClient, pkceOauthClient);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource()
	{
		final KeyPair keyPair = generateRsaKey();
		final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		final RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	private static KeyPair generateRsaKey()
	{
		KeyPair keyPair;
		try
		{
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex)
		{
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean
	public JwtDecoder jwtDecoder(final JWKSource<SecurityContext> jwkSource)
	{
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings()
	{
		return AuthorizationServerSettings.builder().build();
	}

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtEncodingContextCustomizer()
	{
		return (context) -> {
				if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN))
				{
					context.getClaims().claims((claims) -> {
						if (context.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS))
						{
							Set<String> roles = context.getClaims().build().getClaim("scope");
							claims.put("roles", roles);
						}
						else if (context.getAuthorizationGrantType().equals(AuthorizationGrantType.AUTHORIZATION_CODE))
						{
							Set<String> roles = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
									.stream()
									.map(c -> c.replaceFirst("^ROLE_", ""))
									.collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
							claims.put("roles", roles);
						}
					});
				}
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker()
	{
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}
}
