/**
 * 
 */
package org.plaidcat.PPMTool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.plaidcat.PPMTool.security.SecurityConstants.*;

import org.plaidcat.PPMTool.services.CustomUserDetailService;

/**
 * @author nlahndorff
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, jsr250Enabled=true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private CustomUserDetailService detailService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {return new JwtAuthenticationFilter();}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.cors().and().csrf().disable()											//Disable csrf, because we have jwt protection
         .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()   //Send back a controlled response for unauthorized
         .sessionManagement()
         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)                    //Session data maintained in redux, not on server
         .and()
         .headers().frameOptions().sameOrigin()                                     //To enable H2 Database
         .and()
         .authorizeRequests()
         .antMatchers(STATIC_CONTENT).permitAll()
         .antMatchers(SIGNUP_URLS).permitAll()
         .antMatchers(H2_URL).permitAll()
         .anyRequest().authenticated();
		 
		 http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailService).passwordEncoder(bCryptPasswordEncoder);
	}


	//Allows us to use the default manager as a bean.
	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	
}
