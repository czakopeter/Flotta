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
			  .antMatchers("/verification/**", "/registration", "/accessDennied").permitAll()
//				.antMatchers("/billing/**").hasAnyRole("FINANCIAL", "ADMIN")
//				.antMatchers("/subscription/**", "/device/**", "/deviceType/**", "/sim/**").hasAnyRole("MOBILE", "ADMIN")
//				.antMatchers("users/**").hasRole("ADMIN")
//				.antMatchers("/profile/**").authenticated()
//				.antMatchers("/").not().hasAnyRole("CHANGE_PASSWORD", "ANONYMOUS")
//				.anyRequest().authenticated()
			  .antMatchers("/billing/**").hasAnyAuthority("FINANCIAL", "ADMIN")
        .antMatchers("/subscription/**", "/device/**", "/deviceType/**", "/sim/**").hasAnyAuthority("MOBILE", "ADMIN")
        .antMatchers("/users/**").hasAnyAuthority("ADMIN")
        .antMatchers("/profile/**").authenticated()
        .antMatchers("/").hasAnyAuthority("ADMIN", "USER", "FINANCIAL", "MOBIL")
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
		
		
		//adatbázis elérése böngészőből
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
	
	@Bean
  public AccessDeniedHandler accessDeniedHandler() {
      return new CustomAccessDeniedHandler();
  }
	
}
