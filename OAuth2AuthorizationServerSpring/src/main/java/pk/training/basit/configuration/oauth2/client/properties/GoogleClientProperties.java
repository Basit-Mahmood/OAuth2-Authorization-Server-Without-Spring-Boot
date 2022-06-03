package pk.training.basit.configuration.oauth2.client.properties;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleClientProperties {

	@Value("${spring.security.oauth2.client.provider.google.user-name-attribute}")
	private String userNameAttribute;
	
	@Value("${spring.security.oauth2.client.registration.google-idp.provider}")
	private String provider;

	@Value("${spring.security.oauth2.client.registration.google-idp.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.google-idp.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.registration.google-idp.scope}")
	private Set<String> scope;

	@Value("${spring.security.oauth2.client.registration.google-idp.client-name}")
	private String clientName;

	public String getUserNameAttribute() {
		return userNameAttribute;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Set<String> getScope() {
		return scope;
	}

	public void setScope(Set<String> scope) {
		this.scope = scope;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
