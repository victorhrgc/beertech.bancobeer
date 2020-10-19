package beertech.becks.api.security.config;

import beertech.becks.api.security.filter.JwtAuthFilter;
import beertech.becks.api.security.service.JwtService;
import beertech.becks.api.security.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserSecurityService userService;

	@Autowired
	private JwtService jwtService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public OncePerRequestFilter jwtFilter() {
		return new JwtAuthFilter(jwtService, userService);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()

				.antMatchers("/accounts/**").hasAnyRole("ADMIN", "USER")
				.antMatchers("/login/**").permitAll()
				.antMatchers("/transactions/**/statements").hasAnyRole("ADMIN", "USER")
				.antMatchers("/transactions/**/deposit").hasRole("ADMIN")
				.antMatchers("/transactions/**/withdrawal").hasAnyRole("ADMIN", "USER")
				.antMatchers("/transactions/**/transfer").hasAnyRole("ADMIN", "USER")
				.antMatchers("/users/**").hasAnyRole("ADMIN", "USER")

				.and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "swagger-resources/**", "/swagger-ui.html",
				"/webjars/**");
	}
}
