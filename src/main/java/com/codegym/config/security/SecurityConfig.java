package com.codegym.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws
            Exception {
        http.authorizeRequests().antMatchers("/", "/login").permitAll()
                .antMatchers("/product", "/products").authenticated()
                .antMatchers("/product/create").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/product").access("hasRole('ROLE_USER')")
                .and()
                .formLogin().loginPage("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}12345").roles("USER").and()
                .withUser("admin").password("{noop}12345").roles("ADMIN");
    }
}
