package pk.training.basit.oauth2.server.authorization.customizer.jwt;

import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;

import pk.training.basit.oauth2.server.authorization.customizer.jwt.impl.DefaultJwtCustomizerHandler;
import pk.training.basit.oauth2.server.authorization.customizer.jwt.impl.OAuth2AuthenticationTokenJwtCustomizerHandler;
import pk.training.basit.oauth2.server.authorization.customizer.jwt.impl.UsernamePasswordAuthenticationTokenJwtCustomizerHandler;

public interface JwtCustomizerHandler {

	void customize(JwtEncodingContext jwtEncodingContext);
	
	static JwtCustomizerHandler getJwtCustomizerHandler() {
		
		JwtCustomizerHandler defaultJwtCustomizerHandler = new DefaultJwtCustomizerHandler();
		JwtCustomizerHandler oauth2AuthenticationTokenJwtCustomizerHandler = new OAuth2AuthenticationTokenJwtCustomizerHandler(defaultJwtCustomizerHandler);
		JwtCustomizerHandler usernamePasswordAuthenticationTokenJwtCustomizerHandler = new UsernamePasswordAuthenticationTokenJwtCustomizerHandler(oauth2AuthenticationTokenJwtCustomizerHandler);
		return usernamePasswordAuthenticationTokenJwtCustomizerHandler;
		
		
	}
	
}
