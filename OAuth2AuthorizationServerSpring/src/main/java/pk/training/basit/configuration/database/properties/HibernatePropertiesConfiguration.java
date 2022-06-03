package pk.training.basit.configuration.database.properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource("classpath:database/hibernate.properties")
})
public class HibernatePropertiesConfiguration {

}
