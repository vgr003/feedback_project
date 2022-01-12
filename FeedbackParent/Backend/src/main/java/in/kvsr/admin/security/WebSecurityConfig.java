package in.kvsr.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AdminDetailsService adminDetailsService() {
		
		return new AdminDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(adminDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		     .antMatchers("/settings/**").hasAuthority("ADMIN")
		     .antMatchers("/students/**").hasAnyAuthority("ADMIN","HOD")
		     .antMatchers("/hod/**").hasAuthority("ADMIN")
		     .antMatchers("/first-year-faculty/**").hasAuthority("HOD")
		     .antMatchers("/second-year-faculty/**").hasAuthority("HOD")
		     .antMatchers("/third-year-faculty/**").hasAuthority("HOD")
		     .antMatchers("/fourth-year-faculty/**").hasAuthority("HOD")
		     .antMatchers("/error/**").permitAll()
		     .anyRequest()
		     .authenticated()
		     .and()
		         .formLogin().defaultSuccessUrl("/faculty",true)
			     .loginPage("/login")
			     .usernameParameter("regId")
			     .permitAll()
			  .and()
			     .rememberMe().key("qbcdefghijklmnopqrs_0123456789")
			     .userDetailsService(adminDetailsService())
			     .tokenValiditySeconds(7*24*60*60)
			  .and()
			      .logout()
			      .permitAll()
			    ;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**","/js/**","/css/**","/webjars/**");
	}
	
	
	
}
