package pk.training.basit.configuration.context;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.fasterxml.jackson.databind.ObjectMapper;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.decorators.strategies.GroupingStrategy;
import pk.training.basit.controller.web.WebControllerMarker;

/**
 *   	
 * The equivalent xml configuration is this
 * 
 * 		<mvc:annotation-driven />

    	<context:annotation-config />
    	
    	<context:component-scan base-package="pk.training.basit" use-default-filters="false" >
    		<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller" />
    	</context:component-scan>
 *   
 * @author basit.ahmed
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(
		basePackageClasses = {WebControllerMarker.class},
        useDefaultFilters = false,
        		includeFilters = @ComponentScan.Filter({ Controller.class, ControllerAdvice.class })
)
public class WebServletContextConfiguration implements WebMvcConfigurer {
	
	private static final Logger log = LogManager.getLogger();

	 @Autowired
	 private ApplicationContext applicationContext;
	
	@Autowired
	private ObjectMapper objectMapper;
    
	@Autowired
	private Marshaller marshaller;
    
	@Autowired 
	private Unmarshaller unmarshaller;

	@Autowired 
	private SpringValidatorAdapter validator;
	
	@Autowired 
	private MessageSource messageSource;
	
	/**
	 * 
	 * 		<mvc:annotation-driven content-negotiation-manager="contentManager" >
   
        		<mvc:message-converters>
        		
        			<bean class="org.springframework.http.converter.xml.MarshallingHttpMessageConverter"
                		p:marshaller-ref="webProxyJaxbMarshaller"
                		p:unmarshaller-ref="webProxyJaxbMarshaller"/>
                		
            		<bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"
                		p:supportedMediaTypes="application/json" 
            			p:objectMapper-ref="jacksonMapperFactory" />
           
        		</mvc:message-converters>
    		</mvc:annotation-driven>
	 */
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new FormHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());

        MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();
        xmlConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "xml"),
                new MediaType("text", "xml")
        ));
        xmlConverter.setMarshaller(this.marshaller);
        xmlConverter.setUnmarshaller(this.unmarshaller);
        converters.add(xmlConverter);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "json"),
                new MediaType("text", "json")
        ));
        jsonConverter.setObjectMapper(this.objectMapper);
        converters.add(jsonConverter);
    }
	
	/**
	 * 
	 * 		<bean id="contentManager" class="org.springframework.web.accept.ContentNegotiationManagerFactoryBean"
				p:favorPathExtension="true"
				p:favorParameter="true" 
				p:parameterName="mediaType" 
	    		p:ignoreAcceptHeader="false" 
	    		p:defaultContentType="application/json" 
	    		p:useJaf="false"
	    		p:mediaTypes-ref="mediaTypesMap" />
	    		
	    	<util:map id="mediaTypesMap">
    			<entry key="json" value="application/json" />
	 			<entry key="xml" value="application/xml" />
			</util:map>
	 * 
	 */
	@Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
        	.favorParameter(false)
            .parameterName("mediaType")
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON);
    }
	
	@Override
    public Validator getValidator() {
        return this.validator;
    }
	
	/**
	   
	   		<bean id="templateResolver" class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver"
				p:prefix="/WEB-INF/views/" 
				p:suffix=".html" 
				p:templateMode="HTML5"
				p:cacheable="true" />
	  * 
	  */
	@Bean
	@Description("Thymeleaf Template Resolver")
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(this.applicationContext);
	    templateResolver.setPrefix("/WEB-INF/templates/");
	    templateResolver.setSuffix(".html");
	    templateResolver.setTemplateMode(TemplateMode.HTML);
	    templateResolver.setCacheable(true);

	    return templateResolver;
	}
	
	/**
	 * 
	   		<bean id="templateEngine" class="org.thymeleaf.spring5.SpringTemplateEngine"
				p:templateResolver-ref="templateResolver" 
				p:suffix=".html" 
				p:templateMode="HTML5"
				p:cacheable="true" />
	  * 
	  */
	@Bean
	@Description("Thymeleaf Template Engine")
	public SpringTemplateEngine templateEngine() {
	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver());
	    templateEngine.setTemplateEngineMessageSource(messageSource);
	    templateEngine.addDialect(new LayoutDialect(new GroupingStrategy()));
	    return templateEngine;
	}
	
	/**
	 *
	   
	   		<bean id="viewResolver" class="org.thymeleaf.spring4.view.ThymeleafViewResolver"
				p:templateEngine-ref="templateEngine" 
				p:order="1" 
				p:viewNames="*.html,*.xhtml" />
	  * 
	  */
	@Bean
	@Description("Thymeleaf View Resolver")
	public ThymeleafViewResolver viewResolver() {
	    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	    viewResolver.setTemplateEngine(templateEngine());
	    viewResolver.setOrder(1);
	    return viewResolver;
	}
	 
	/**
	 * 
	   		<bean id="viewNameTranslator" class="org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator.DefaultRequestToViewNameTranslator" />
	 * 
	 * @return
	 */
	@Bean
	public RequestToViewNameTranslator viewNameTranslator() {
		return new DefaultRequestToViewNameTranslator();
	}
	
	
	
	/**
     *
	   		<bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver" />
	 * 
	 */
	@Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
	
	/**
	 *
	 * 	<interceptors>     
        	<beans:bean class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor" p:paramName="lang" />
    	</interceptors>
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(new LocaleChangeInterceptor());
	}
	
	/**
	 * 
	 * 		<mvc:resources mapping="/resources/**", "/webjars/**" location="/resources/", "/webjars/"/>
	 */
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**", "/static/**", "/webjars/**")
        .addResourceLocations("/resources/", "/static/", "/webjars/")
        .resourceChain(false); 
    }
	
	
	/**
	 * 		
			<beans:bean id="localeResolver" class="org.springframework.web.servlet.i18n.SessionLocaleResolver.SessionLocaleResolver"  
        		p:cookieName="locale" />
	 * 
	 * 
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}
	
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        
		Integer page = 0;
		Integer size = 10;
		
		Sort defaultSort = Sort.by(Sort.Direction.DESC, "id");
        Pageable defaultPageable = PageRequest.of(page, size, defaultSort);

        SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
        sortResolver.setSortParameter("paging.sort");
        sortResolver.setFallbackSort(defaultSort);

        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver(sortResolver);
        pageableResolver.setMaxPageSize(100);
        pageableResolver.setOneIndexedParameters(true);
        pageableResolver.setPrefix("paging.");
        pageableResolver.setFallbackPageable(defaultPageable);

        resolvers.add(sortResolver);
        resolvers.add(pageableResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
    	
        if(!(registry instanceof FormattingConversionService)) {
            log.warn("Unable to register Spring Data JPA converter.");
            return;
        }

        DomainClassConverter<FormattingConversionService> converter = new DomainClassConverter<>((FormattingConversionService)registry);
        converter.setApplicationContext(this.applicationContext);
    }
	
}
