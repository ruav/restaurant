package com.restaurant.config;

import com.restaurant.handler.CustomAccessDeniedHandler;
import com.restaurant.utils.CustomUserDetailsService;
import com.restaurant.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                http
                .authorizeRequests()
                .antMatchers( "/home").permitAll()
//                .antMatchers( "/registration").permitAll()
                .antMatchers( "/forgetPassword").permitAll()
                .antMatchers( "/forgot-password**").permitAll()
                .antMatchers( "/reset-password**").permitAll()
                .antMatchers("/js/**", "/css/**", "/img/**", "/icon/*").permitAll()
                .antMatchers("/rest/**").permitAll()
                .antMatchers("/image/**").permitAll()
                .antMatchers("/qr", "qr2").permitAll()
                .antMatchers("/users/**").hasAuthority(Role.Root.name())
                .anyRequest().authenticated()
        ;
        http
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().logoutUrl("/logout") .logoutSuccessUrl("/main")
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

}