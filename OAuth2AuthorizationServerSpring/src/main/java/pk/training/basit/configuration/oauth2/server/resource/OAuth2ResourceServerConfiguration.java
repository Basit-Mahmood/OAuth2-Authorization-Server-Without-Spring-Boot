package pk.training.basit.configuration.oauth2.server.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import pk.training.basit.oauth2.server.resource.converter.jwt.CustomJwtGrantedAuthoritiesConverter;

//@EnableGlobalMethodSecurity(
//	prePostEnabled = true, 
//	order = 0
//)
@EnableWebSecurity
@PropertySource("classpath:oauth2-resource-server.properties")
public class OAuth2ResourceServerConfiguration {

	@Value("${jwk.set.uri}")
    private String jwkSetUri;
	
	@Bean
	public JwtDecoder jwtDecoder() {
    	return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}
   
    JwtAuthenticationConverter jwtAuthenticationConverter() {
    	CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.mvcMatcher("/messages/**").authorizeRequests()
		    .anyRequest().authenticated()
		            //.mvcMatchers("/messages/**").access("hasAuthority('USER')")
			        .and()
			.oauth2ResourceServer(oauth2 -> oauth2
			    .jwt(jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))
			);
		
		return http.build();
	}
	
}
