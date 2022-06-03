package pk.training.basit.configuration.federated.identity;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:federated-identity.properties")
public class FederatedIdentityConfiguration {

}
