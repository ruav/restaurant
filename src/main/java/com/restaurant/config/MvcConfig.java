package com.restaurant.config;

import com.restaurant.handler.CustomLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
@EnableWebSecurity
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("/home");
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/hello").setViewName("/hello");
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/registration").setViewName("/registration");
        registry.addViewController("/main").setViewName("/index");
        registry.addViewController("/qr2").setViewName("/qr2");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public Session getJavaMailSession() {


        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.user", "");
        props.put("mail.smtp.password", "");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", true);

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication  getPasswordAuthentication() {
                return new PasswordAuthentication(
                        props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.password"));
            }
        });
    }

}