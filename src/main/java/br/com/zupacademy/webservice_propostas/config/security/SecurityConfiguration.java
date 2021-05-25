package br.com.zupacademy.webservice_propostas.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests(authorizeRequests -> {
				authorizeRequests
					.antMatchers(HttpMethod.POST, "/propostas").permitAll()
					.antMatchers(HttpMethod.GET, "/actuator/**").hasRole("admin")
					.anyRequest().authenticated();
			
		}).oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
		http.csrf().disable();
	}
	
	private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }
//	
	//Não precisa. Uri já está no application.properties
//	@Bean
//    JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
//    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/swagger-ui-custom.html", "/v3/api-docs/**", "/swagger-ui/**");
	}
}
