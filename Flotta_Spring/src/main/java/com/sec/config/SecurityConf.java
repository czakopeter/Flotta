package com.sec.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
			//TODO adatbázishoz csak ADMIN, .antMatchers("/db/**").hasRole("ADMIN")
			  .antMatchers("/db/**").permitAll()
			  .antMatchers("/verifyAndChangePassword/**", "/registration").permitAll()
				.antMatchers("/billing/**").hasAnyRole("FINANCIAL", "ADMIN")
				.antMatchers("/subscription/**", "/device/**", "/deviceType/**").hasAnyRole("MOBILE", "ADMIN")
				.antMatchers("/profile/**").authenticated()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?logout")
				.permitAll()
		  .and()
		    .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		
		
		//adatbázis elérése böngészőből
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
	
	@Bean
  public AccessDeniedHandler accessDeniedHandler() {
      return new CustomAccessDeniedHandler();
  }
	
}
