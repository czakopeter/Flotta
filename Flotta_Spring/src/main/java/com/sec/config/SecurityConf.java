package com.sec.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConf extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
	    return super.userDetailsService();
	}
	
	@Autowired
	private UserDetailsService userService;
	
	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			//TODO adatbázishoz csak ADMIN,
//			  .antMatchers("/db/**").hasRole("ADMIN")
			  .antMatchers("/db/**").permitAll()
			  .antMatchers("/activation/**", "/registration", "/accessDennied", "/passwordReset").permitAll()
			  .antMatchers("/billing/**", "/finance/**").hasAnyAuthority("FINANCE_MNGR", "ADMIN")
			  .antMatchers("/subscription/**", "/sim/**").hasAnyAuthority("SUBSCRIPTION_MNGR", "ADMIN")
        .antMatchers("/device/**", "/deviceType/**").hasAnyAuthority("DEVICE_MNGR", "ADMIN")
        .antMatchers("/user/**").hasAnyAuthority("USER_MNGR","ADMIN")
        .antMatchers("/profile/**", "/").hasAnyAuthority("BASIC", "PASSWORD")
        .anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.failureUrl("/loginError")
				//belépést követően a kezdőlapra irányítsa át
				.defaultSuccessUrl("/", false)
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?logout")
				.permitAll()
		  .and()
		    .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		
		
		http.sessionManagement().maximumSessions(3).sessionRegistry(getSessionRegistry());
		
		//adatbázis elérése böngészőből
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
	
	@Bean
  public AccessDeniedHandler accessDeniedHandler() {
      return new CustomAccessDeniedHandler();
  }
	
	@Bean SessionRegistry getSessionRegistry() {
	  return new SessionRegistryImpl();
	}
	
}
