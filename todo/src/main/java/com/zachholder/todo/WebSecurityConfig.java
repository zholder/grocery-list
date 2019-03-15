package com.zachholder.todo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DataSource dataSource;
    
    
    @Autowired
    public void configurationGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception{
    	authenticationMgr.jdbcAuthentication().dataSource(dataSource)
    	.passwordEncoder(new BCryptPasswordEncoder())
		.usersByUsernameQuery("select email, password, enabled from user where email=?")
	    .authoritiesByUsernameQuery("select email, 'ROLE_USER' from user where email=?");
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
//        When deploying and or moving away from web only:
//        https://stackoverflow.com/questions/19468209/spring-security-403-error
            .authorizeRequests()
//                URLs below would be allowed without authentication
                .antMatchers("/css/**", "/signup/**").permitAll() 
                .anyRequest().authenticated()                
                .and()
                
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            
            .logout()
                .permitAll();
    }
    

}
