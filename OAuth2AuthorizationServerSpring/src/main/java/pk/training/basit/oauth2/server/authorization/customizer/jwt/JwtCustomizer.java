package pk.training.basit.oauth2.server.authorization.customizer.jwt;

import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;

public interface JwtCustomizer {

	void customizeToken(JwtEncodingContext context);
	
}
