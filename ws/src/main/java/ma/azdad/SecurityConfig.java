package ma.azdad;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import ma.azdad.model.Role;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Value("${applicationCode}")
	public String applicationCode;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().cacheControl().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests() //
				.antMatchers("/resources/**", "/login.xhtml", "/blank", "/rest/**", "/resttest/**", "/passwordReset.xhtml").permitAll() //
				.antMatchers("/scripts.xhtml").hasRole(Role.ROLE_IT_MANAGER.getRole()) //
				.antMatchers("/**").hasRole(applicationCode) //
				.and().formLogin().loginPage("/login.xhtml").successForwardUrl("/smsList.xhtml?faces-redirect=true").defaultSuccessUrl("/smsList.xhtml?faces-redirect=true", true) //
				.failureUrl("/login.xhtml?error1").and().exceptionHandling().accessDeniedPage("/login.xhtml?error2").and().logout().permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		String usernameQuery = "select login,password, 1 from users where login=?";
//		String rolesQuery = "select b.login,a.role from user_role a,users b where a.user_username = b.username and b.login=?";
//		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(usernameQuery).authoritiesByUsernameQuery(rolesQuery);
		String usernameQuery = "select username,password, 1 from users where username=?";
		String rolesQuery = "select b.username,a.authority  from authorities a,users b where a.username = b.username and b.username=?";
		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(usernameQuery).authoritiesByUsernameQuery(rolesQuery);
	}

	private String[] getPages(String... models) {
		String[] pages = new String[3 * models.length];
		for (int i = 0; i < models.length; i++) {
			String str1 = models[i];
			String str2 = str1.substring(0, 1).toLowerCase() + str1.substring(1);
			pages[i * 3] = "/" + str2 + "List.xhtml";
			pages[i * 3 + 1] = "/addEdit" + str1 + ".xhtml";
			pages[i * 3 + 2] = "/view" + str1 + ".xhtml";
		}
		return pages;
	}

	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
}