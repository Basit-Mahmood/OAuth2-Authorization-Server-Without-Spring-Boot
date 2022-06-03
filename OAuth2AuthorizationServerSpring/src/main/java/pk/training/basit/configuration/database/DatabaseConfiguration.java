package pk.training.basit.configuration.database;

import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import pk.training.basit.configuration.database.properties.DatabasePropertiesConfiguration;

@Configuration


@EnableTransactionManagement(mode = AdviceMode.PROXY, proxyTargetClass = false, order = Ordered.LOWEST_PRECEDENCE)
@EnableJpaRepositories(
	basePackages = "pk.training.basit.jpa.repository",
    entityManagerFactoryRef = "entityManagerFactory",
	transactionManagerRef = "transactionManager"
)
@Import({DatabasePropertiesConfiguration.class})
public class DatabaseConfiguration {
      
	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
	private static final String PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
	private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
	private static final String[] ENTITYMANAGER_PACKAGES_TO_SCAN = {"pk.training.basit.jpa.entity"};
	   
	@Autowired
	private Environment env;
	
	/**
	 *
	@Bean(destroyMethod = "close")
	public DataSource dataSource(){
	    
		HikariConfig hikariConfig = new HikariConfig();
	    hikariConfig.setDriverClassName(env.getProperty("jdbc.driverClassName"));
	    hikariConfig.setJdbcUrl(env.getProperty("jdbc.url")); 
	    hikariConfig.setUsername(env.getProperty("jdbc.username"));
	    hikariConfig.setPassword(env.getProperty("jdbc.password"));

	    hikariConfig.setMaximumPoolSize(5);
	    hikariConfig.setConnectionTestQuery("SELECT 1");
	    hikariConfig.setPoolName("springHikariCP");

	    hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
	    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
	    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
	    hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

	    HikariDataSource dataSource = new HikariDataSource(hikariConfig);

	    return dataSource;
    }
	*/
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(embeddedDataSource());
		return namedParameterJdbcTemplate;
	}
	
	@Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(embeddedDataSource());
    }
	
	 /**
	  * 		
				<bean id="jpaTransactionManager" class="org.springframework.orm.jpa.JpaTransactionManager"
	        		p:entityManagerFactory-ref="entityManagerFactory"/>
	  * 
	  */
	 @Bean
     public JpaTransactionManager transactionManager() {
		 JpaTransactionManager transactionManager = new JpaTransactionManager();
         transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
         return transactionManager;
     }
	 
	 
    @Bean
	public HibernateJpaVendorAdapter vendorAdaptor() {
		 HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		 return vendorAdapter;
	}
	 
	/**
	 * 		
			<bean id="ls360Emf" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean" 
        		p:dataSource-ref="customerSupportDataSource" 
        		p:jpaVendorAdapter-ref="vendorAdaptor"          
        		p:packagesToScan="pk.training.basit.site.entities" 
        		p:jpaProperties-ref="jpaHibernateProperties" />
	 * 
	 * @return
	 */
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		 
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
        entityManagerFactoryBean.setDataSource(embeddedDataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);  
        entityManagerFactoryBean.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        entityManagerFactoryBean.setValidationMode(ValidationMode.NONE);
        entityManagerFactoryBean.setJpaProperties(jpaHibernateProperties());
      
        return entityManagerFactoryBean;
     }
	 
	 private Properties jpaHibernateProperties() {
         Properties properties = new Properties();
         properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
         properties.put(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH, env.getProperty(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH));
         properties.put(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE, env.getProperty(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE));
         properties.put(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE, env.getProperty(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE));
         
         return properties;       
	 }
	 
    /**
     * http://127.0.0.1:9090/OAuth2AuthorizationServerSpring/h2-console
     * 
     * No password required in h2-console
     * 
     * url     : jdbc:h2:mem:testdb
	   username: sa
     * @return
     */
	@Bean(destroyMethod = "shutdown")
	public DataSource embeddedDataSource() {
		
		return new EmbeddedDatabaseBuilder()
			.generateUniqueName(false)
			.setName("testdb")
			.setType(EmbeddedDatabaseType.H2)
			.setScriptEncoding("UTF-8")
			.addScript("classpath:/org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
			.addScript("classpath:/org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
			.addScript("classpath:/org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
			.addScript("classpath:/database/scripts/schema-h2.sql")
			.addScript("classpath:/database/scripts/test-data.sql")
			.build();
	}
	
}
