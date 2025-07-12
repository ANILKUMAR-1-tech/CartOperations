package com.eCommerce.CartOperations.security.config;

import com.eCommerce.CartOperations.security.jwt.JwtEntryPoint;
import com.eCommerce.CartOperations.security.jwt.JwtTokenFilter;
import com.eCommerce.CartOperations.security.jwt.JwtUtils;
import com.eCommerce.CartOperations.security.user.EcomUserDetailService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class CartConfig {
    private final JwtEntryPoint jwtEntryPoint;
    private final EcomUserDetailService userDetailService;
    private final JwtUtils jwtUtils;

    private static final List<String> SEURED_URLS=List.of( "/api/carts/**",
            "/api/orders/**");

    @Bean
    public ModelMapper modelMapper(){
        return  new ModelMapper();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter(jwtUtils,userDetailService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       return httpSecurity
               .csrf(AbstractHttpConfigurer::disable)
               .exceptionHandling(ex-> ex.authenticationEntryPoint(jwtEntryPoint))
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers(SEURED_URLS.toArray(String[]::new)).authenticated()
                       .anyRequest().permitAll())
               .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
               .build();


    }
}
