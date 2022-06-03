package pk.training.basit.configuration.context;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import pk.training.basit.configuration.database.DatabaseConfiguration;
import pk.training.basit.configuration.database.JpaAuditingConfiguration;
import pk.training.basit.service.ServiceMarker;

/**	
 *   	
 * The equivalent xml configuration is this
 * 
 * 		<?xml version="1.0" encoding="UTF-8"?>
		<beans xmlns="http://www.springframework.org/schema/beans"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:data-jpa="http://www.springframework.org/schema/data/jpa"
			xsi:schemaLocation="http://www.springframework.org/schema/beans
			http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
			http://www.springframework.org/schema/data/jpa
			http://www.springframework.org/schema/data/jpa/spring-jpa-1.3.xsd">

				<context:annotation-config />
    
    			<context:component-scan base-package="pk.training.basit.site" >
        			<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller" />
    			</context:component-scan>
				
				<data-jpa:repositories base-package="com.sample" 
					transaction-manager-ref="jpaTransactionManager" 
					entity-manager-factory-ref="entityManagerFactoryBean"/>	
		</beans>
 *   
 * @author basit.ahmed
 *
 */
@Configuration
@ComponentScan(
	basePackageClasses = {ServiceMarker.class},
	excludeFilters = @ComponentScan.Filter({
		Controller.class, ControllerAdvice.class, RestController.class, RestControllerAdvice.class
	})
)
@Import({DatabaseConfiguration.class, 
	JpaAuditingConfiguration.class,  
	SecurityConfiguration.class
})
public class RootContextConfiguration{
	
	@Bean
    public MessageSource messageSource() {
		
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(-1);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames("/WEB-INF/i18n/titles", 
        		"/WEB-INF/i18n/messages",
        		"/WEB-INF/i18n/errors", 
        		"/WEB-INF/i18n/validation",
        		"classpath:org/springframework/security/messages"
        );
        return messageSource;
    }
	
	/**
	 * 		
			<bean id="localValidatorFactoryBean" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"
        		p:validationMessageSource-ref="messageSource" />
	 * 
	 */
	@Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(this.messageSource());
        return validator;
    }
	
	/**
	 *  
	        <bean id="methodValidationPostProcessor" class="org.springframework.validation.beanvalidation.MethodValidationPostProcessor"
        		p:validator-ref="localValidatorFactoryBean" />
	 * 
	 * @return
	 */
	@Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(this.localValidatorFactoryBean());
        return processor;
    }
	
	/**
	 * 		
			<bean id="objectMapper" class="com.fasterxml.jackson.databind.ObjectMapper"
				p:serializationInclusion="#{T(com.fasterxml.jackson.annotation.JsonInclude.Include).NON_NULL}"  />
	 * 
	 * @return
	 */
	@Bean
    public ObjectMapper objectMapper() {
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        return mapper;
    }
	
	@Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
    	
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(new String[] { "pk.training.basit.jpa.entities" });
        return marshaller;
    }
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
	@Bean
	public ConversionService conversionService() {
	    return new DefaultConversionService();
	}
		
}
