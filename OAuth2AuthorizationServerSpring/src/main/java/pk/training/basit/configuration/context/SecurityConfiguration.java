package pk.training.basit.configuration.context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import pk.training.basit.configuration.federated.identity.FederatedIdentityConfiguration;
import pk.training.basit.configuration.federated.identity.FederatedIdentityConfigurer;
import pk.training.basit.configuration.federated.identity.UserRepositoryOAuth2UserHandler;
import pk.training.basit.configuration.oauth2.client.OAuth2LoginConfiguration;
import pk.training.basit.configuration.oauth2.server.authorization.AuthorizationServerConfiguration;
import pk.training.basit.configuration.oauth2.server.resource.OAuth2ResourceServerConfiguration;
import pk.training.basit.service.UserPrincipalService;

@EnableGlobalMethodSecurity(
    prePostEnabled = true, 
    order = 0, 
    mode = AdviceMode.PROXY,
    proxyTargetClass = false
)
@EnableWebSecurity
@Import({
	AuthorizationServerConfiguration.class,
	FederatedIdentityConfiguration.class, 
	OAuth2LoginConfiguration.class,
	OAuth2ResourceServerConfiguration.class, 
})
public class SecurityConfiguration {

	private static final Logger LOGGER = LogManager.getLogger(SecurityConfiguration.class);

	@Autowired 
	private UserPrincipalService userPrincipalService;
	
	// If no passwordEncoder bean is defined then you have to prefix password like {noop}secret1, or {bcrypt}password
	// if not static spring boot 2.6.x gives bean currently in creation error at line .passwordEncoder(passwordEncoder()) in configureGlobal() method
	/**
	@Bean
    public static PasswordEncoder passwordEncoder() {		
		LOGGER.debug("in passwordEncoder");
        return new BCryptPasswordEncoder();
    };
    */
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		LOGGER.debug("in configureGlobal");
		 builder
             .userDetailsService(this.userPrincipalService)
                // .passwordEncoder(passwordEncoder())
         .and()
             .eraseCredentials(true);
	}
	
	//@Bean
	//public WebSecurityCustomizer webSecurityCustomizer() {
		//return (web) -> web.ignoring().antMatchers("/resources/**", "/static/**", "/webjars/**");
	//}
	
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		LOGGER.debug("in configure HttpSecurity");
		
		FederatedIdentityConfigurer federatedIdentityConfigurer = new FederatedIdentityConfigurer().oauth2UserHandler(new UserRepositoryOAuth2UserHandler());
		
		http.authorizeRequests(authorizeRequests -> authorizeRequests.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
			.antMatchers("/resources/**", "/static/**", "/webjars/**").permitAll()
		    .anyRequest().authenticated()
		)
		.formLogin(form -> form.loginPage("/login").failureUrl("/login-error").permitAll())
		.csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
		.and().headers().frameOptions().sameOrigin()
		.and()
		.apply(federatedIdentityConfigurer);
		
		return http.build();
	}
	
}
