package pk.training.basit.configuration.oauth2.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import pk.training.basit.configuration.oauth2.client.properties.OAuth2ClientProperties;

@Configuration(proxyBeanMethods = false)
public class OAuth2LoginClientRegistrationRepositoryConfiguration {

	@Bean
	InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
		List<ClientRegistration> registrations = new ArrayList<>(
				OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties).values());
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
}
