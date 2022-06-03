package pk.training.basit.configuration.oauth2.client;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(
    basePackageClasses = {OAuth2ClientMarker.class}
)
@Import({ OAuth2LoginClientRegistrationRepositoryConfiguration.class})
public class OAuth2LoginConfiguration {
    
}
