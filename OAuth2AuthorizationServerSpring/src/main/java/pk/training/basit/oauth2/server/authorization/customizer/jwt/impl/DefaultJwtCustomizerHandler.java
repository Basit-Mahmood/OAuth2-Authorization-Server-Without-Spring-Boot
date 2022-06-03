package pk.training.basit.oauth2.server.authorization.customizer.jwt.impl;

import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;

import pk.training.basit.oauth2.server.authorization.customizer.jwt.JwtCustomizerHandler;

public class DefaultJwtCustomizerHandler implements JwtCustomizerHandler {

	@Override
	public void customize(JwtEncodingContext jwtEncodingContext) {
		// does not modify any thing in context

	}

}
