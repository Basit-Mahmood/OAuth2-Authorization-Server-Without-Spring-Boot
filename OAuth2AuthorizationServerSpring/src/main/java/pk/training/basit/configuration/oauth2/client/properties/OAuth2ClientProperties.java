package pk.training.basit.configuration.oauth2.client.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class OAuth2ClientProperties {

	private final GoogleClientProperties googleClientProperties;
	private final GithubClientProperties githubClientProperties;
	
	private final Map<String, Provider> provider = new HashMap<>();
	private final Map<String, Registration> registration = new HashMap<>();

	public OAuth2ClientProperties(GoogleClientProperties googleClientProperties, GithubClientProperties githubClientProperties) {
		
		this.googleClientProperties = googleClientProperties;
		this.githubClientProperties = githubClientProperties;
		
		setOAuth2ClientProvider();
		setOAuth2ClientRegistration();
	}
	
	public Map<String, Provider> getProvider() {
		return this.provider;
	}

	public Map<String, Registration> getRegistration() {
		return this.registration;
	}
	
	private void setOAuth2ClientRegistration() {
		
		Registration googleClientRegistration = new Registration();
		googleClientRegistration.setProvider("GOOGLE");
		googleClientRegistration.setClientId(googleClientProperties.getClientId());
		googleClientRegistration.setClientName(googleClientProperties.getClientName());
		googleClientRegistration.setClientSecret(googleClientProperties.getClientSecret());
		googleClientRegistration.setScope(googleClientProperties.getScope());
		
		Registration githubClientRegistration = new Registration();
		githubClientRegistration.setProvider("GITHUB");
		githubClientRegistration.setClientId(githubClientProperties.getClientId());
		githubClientRegistration.setClientSecret(githubClientProperties.getClientSecret());
		githubClientRegistration.setClientName(githubClientProperties.getClientName());
		githubClientRegistration.setScope(githubClientProperties.getScope());
		
		registration.put("google-idp", googleClientRegistration);
		registration.put("github-idp", githubClientRegistration);
		
	}
	
	private void setOAuth2ClientProvider() {
		
		Provider googleProvider = new Provider();
		googleProvider.setUserNameAttribute(googleClientProperties.getUserNameAttribute());
		
		Provider githubProvider = new Provider();
		googleProvider.setUserNameAttribute(githubClientProperties.getUserNameAttribute());
		
		provider.put("google", googleProvider);
		provider.put("github", githubProvider);
		
	}
	
	/**
	 * A single client registration.
	 */
	public static class Registration {

		/**
		 * Reference to the OAuth 2.0 provider to use. May reference an element from the
		 * 'provider' property or used one of the commonly used providers (google,
		 * github, facebook, okta).
		 */
		private String provider;

		/**
		 * Client ID for the registration.
		 */
		private String clientId;

		/**
		 * Client secret of the registration.
		 */
		private String clientSecret;

		/**
		 * Client authentication method. May be left blank when using a pre-defined
		 * provider.
		 */
		private String clientAuthenticationMethod;

		/**
		 * Authorization grant type. May be left blank when using a pre-defined
		 * provider.
		 */
		private String authorizationGrantType;

		/**
		 * Redirect URI. May be left blank when using a pre-defined provider.
		 */
		private String redirectUri;

		/**
		 * Authorization scopes. When left blank the provider's default scopes, if any,
		 * will be used.
		 */
		private Set<String> scope;

		/**
		 * Client name. May be left blank when using a pre-defined provider.
		 */
		private String clientName;

		public String getProvider() {
			return this.provider;
		}

		public void setProvider(String provider) {
			this.provider = provider;
		}

		public String getClientId() {
			return this.clientId;
		}

		public void setClientId(String clientId) {
			this.clientId = clientId;
		}

		public String getClientSecret() {
			return this.clientSecret;
		}

		public void setClientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
		}

		public String getClientAuthenticationMethod() {
			return this.clientAuthenticationMethod;
		}

		public void setClientAuthenticationMethod(String clientAuthenticationMethod) {
			this.clientAuthenticationMethod = clientAuthenticationMethod;
		}

		public String getAuthorizationGrantType() {
			return this.authorizationGrantType;
		}

		public void setAuthorizationGrantType(String authorizationGrantType) {
			this.authorizationGrantType = authorizationGrantType;
		}

		public String getRedirectUri() {
			return this.redirectUri;
		}

		public void setRedirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
		}

		public Set<String> getScope() {
			return this.scope;
		}

		public void setScope(Set<String> scope) {
			this.scope = scope;
		}

		public String getClientName() {
			return this.clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

	}
	
	public static class Provider {

		/**
		 * Authorization URI for the provider.
		 */
		private String authorizationUri;

		/**
		 * Token URI for the provider.
		 */
		private String tokenUri;

		/**
		 * User info URI for the provider.
		 */
		private String userInfoUri;

		/**
		 * User info authentication method for the provider.
		 */
		private String userInfoAuthenticationMethod;

		/**
		 * Name of the attribute that will be used to extract the username from the call
		 * to 'userInfoUri'.
		 */
		private String userNameAttribute;

		/**
		 * JWK set URI for the provider.
		 */
		private String jwkSetUri;

		/**
		 * URI that can either be an OpenID Connect discovery endpoint or an OAuth 2.0
		 * Authorization Server Metadata endpoint defined by RFC 8414.
		 */
		private String issuerUri;

		public String getAuthorizationUri() {
			return this.authorizationUri;
		}

		public void setAuthorizationUri(String authorizationUri) {
			this.authorizationUri = authorizationUri;
		}

		public String getTokenUri() {
			return this.tokenUri;
		}

		public void setTokenUri(String tokenUri) {
			this.tokenUri = tokenUri;
		}

		public String getUserInfoUri() {
			return this.userInfoUri;
		}

		public void setUserInfoUri(String userInfoUri) {
			this.userInfoUri = userInfoUri;
		}

		public String getUserInfoAuthenticationMethod() {
			return this.userInfoAuthenticationMethod;
		}

		public void setUserInfoAuthenticationMethod(String userInfoAuthenticationMethod) {
			this.userInfoAuthenticationMethod = userInfoAuthenticationMethod;
		}

		public String getUserNameAttribute() {
			return this.userNameAttribute;
		}

		public void setUserNameAttribute(String userNameAttribute) {
			this.userNameAttribute = userNameAttribute;
		}

		public String getJwkSetUri() {
			return this.jwkSetUri;
		}

		public void setJwkSetUri(String jwkSetUri) {
			this.jwkSetUri = jwkSetUri;
		}

		public String getIssuerUri() {
			return this.issuerUri;
		}

		public void setIssuerUri(String issuerUri) {
			this.issuerUri = issuerUri;
		}

	}

}
