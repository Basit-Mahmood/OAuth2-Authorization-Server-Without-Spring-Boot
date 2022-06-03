package pk.training.basit.configuration.database.properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource("classpath:database/jdbc.properties")
})
@Import({HibernatePropertiesConfiguration.class})
public class DatabasePropertiesConfiguration {

}
