package ma.gcom.testspringjpa;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {
	 * 
	 * auth.inMemoryAuthentication() .withUser("salma") .password("salma")
	 * .roles("USER") .and() .withUser("doha") .password("doha") .roles("ADMIN");
	 * 
	 * 
	 * }
	 * 
	 * @Bean public PasswordEncoder getPasswordEncoder() { return
	 * NoOpPasswordEncoder.getInstance(); }
	 * 
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.authorizeRequests() .antMatchers("/user").permitAll()
	 * .antMatchers("/insertUser").hasRole("ADMIN") .and().formLogin(); }
	 */

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().cacheControl().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests().//
				antMatchers("/insertUser").hasRole("ADMIN").//
				antMatchers("/**").hasAnyRole("USER", "ADMIN")//
				.and().exceptionHandling().accessDeniedPage("/ad.html").and().formLogin().defaultSuccessUrl("/user", true);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		String usersByUsernameQuery = "select login,password, enabled from user where login=?";
		String authoritiesByUsernameQuery = "select login,role  from user_role ,user u where user_id = u.id and login = ?";
		auth.jdbcAuthentication().//
				dataSource(dataSource).//
				usersByUsernameQuery(usersByUsernameQuery).//
				authoritiesByUsernameQuery(authoritiesByUsernameQuery).//
				passwordEncoder(new MessageDigestPasswordEncoder("MD5"));

	}

}