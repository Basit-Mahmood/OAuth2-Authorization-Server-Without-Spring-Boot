package pk.training.basit.configuration.bootstrap;

import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.server.web.WebServlet;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import pk.training.basit.configuration.context.RestServletContextConfiguration;
import pk.training.basit.configuration.context.RootContextConfiguration;
import pk.training.basit.configuration.context.WebServletContextConfiguration;
import pk.training.basit.filter.web.PreSecurityLoggingFilter;

/**
 * 
 * The equivalent XML file
 * 
 * 		<context-param>
        	<param-name>contextConfigLocation</param-name>
        	<param-value>/WEB-INF/spring/rootContext.xml</param-value>
    	</context-param>
    	
    	<listener>
        	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    	</listener>
 * 
    	<servlet>
        	<servlet-name>springWebDispatcher</servlet-name>
        	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        	<init-param>
            	<param-name>contextConfigLocation</param-name>
            	<param-value>/WEB-INF/spring/servletContext.xml</param-value>
        	</init-param>
        	<load-on-startup>1</load-on-startup>
    	</servlet>
    	
    	 <servlet-mapping>
        	<servlet-name>springWebDispatcher</servlet-name>
        	<url-pattern>/</url-pattern>
    	</servlet-mapping>
    	
    	<servlet>
        	<servlet-name>springRestDispatcher</servlet-name>
        	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        	    <init-param>
            	    <param-name>contextConfigLocation</param-name>
            		<param-value>/WEB-INF/spring/appServlet/restful-context.xml</param-value>
        		</init-param>
        	<load-on-startup>2</load-on-startup>
    	</servlet>
    	
    	servlet-mapping>
        	<servlet-name>springRestDispatcher</servlet-name>
        	<url-pattern>/rest/*</url-pattern>
    	</servlet-mapping>
    	
    	<servlet>
        	<servlet-name>springSoapDispatcher</servlet-name>
        	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        	    <init-param>
            	    <param-name>contextConfigLocation</param-name>
            		<param-value>/WEB-INF/spring/appServlet/soap-context.xml</param-value>
        		</init-param>
        	<load-on-startup>3</load-on-startup>
    	</servlet>
    	
    	servlet-mapping>
        	<servlet-name>springSoapDispatcher</servlet-name>
        	<url-pattern>/soap/*</url-pattern>
    	</servlet-mapping>
    	
    	<filter>
        	<filter-name>preSecurityLoggingFilter</filter-name>
        	<filter-class>pk.training.basit.site.filter.PreSecurityLoggingFilter</filter-class>
    	</filter>
    	
    	<filter-mapping>
        	<filter-name>preSecurityLoggingFilter</filter-name>
        	<url-pattern>/*</url-pattern>
    	</filter-mapping>
    	
    	
 * @author basit.ahmed   	
 */
@Order(1)
public class FrameworkBootstrap implements WebApplicationInitializer {

	private static final Logger log = LogManager.getLogger();
	
	@Override
    public void onStartup(ServletContext container) throws ServletException {
        
		log.info("Executing framework bootstrap.");
		
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContextConfiguration.class);
        container.addListener(new ContextLoaderListener(rootContext));

       
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebServletContextConfiguration.class);
        
        ServletRegistration.Dynamic dispatcher = container.addServlet("springWebDispatcher", new DispatcherServlet(webContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.setMultipartConfig(new MultipartConfigElement(null, 20_971_520L, 41_943_040L, 512_000));
        dispatcher.addMapping("/");
        
        
        AnnotationConfigWebApplicationContext restContext = new AnnotationConfigWebApplicationContext();
        restContext.register(RestServletContextConfiguration.class);
        
        DispatcherServlet servlet = new DispatcherServlet(restContext);
        servlet.setDispatchOptionsRequest(true);
        dispatcher = container.addServlet("springRestDispatcher", servlet);
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/rest/*");
        
        // http://127.0.0.1:9090/OAuth2AuthorizationServerSpring/h2-console
        ServletRegistration.Dynamic h2Servlet = container.addServlet("h2-console", new WebServlet());
        h2Servlet.setLoadOnStartup(3);
        h2Servlet.addMapping("/h2-console/*");
        
        FilterRegistration.Dynamic registration = container.addFilter("preSecurityLoggingFilter", new PreSecurityLoggingFilter());
        registration.addMappingForUrlPatterns(null, false, "/*");

    }
	
}