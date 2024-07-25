package me.dio.innovation.one.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder encoderPassword(){
        return new BCryptPasswordEncoder();
    }

    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
    };

    private static final String[] PUBLIC_URLS = {
            "/sessions/login",
            "/h2-console/**",
            "/users/create"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers(HttpMethod.POST, "/sessions/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/total/").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/total/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/total/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/total/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/payments/").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/payments/").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/payments/").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/payments/").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/products/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/products/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/categories/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/categories/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/categories/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/categories/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/users/getAll/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/users/get/{id}/").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/users/create/").hasAnyRole("USER", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/users/update/{id}/").access(new WebExpressionAuthorizationManager("#id == authentication.principal.id or hasAuthority('ROLE_MANAGER')"))
                        .requestMatchers(HttpMethod.DELETE, "/users/delete/{id}/").access(new WebExpressionAuthorizationManager("#id == authentication.principal.id or hasAuthority('ROLE_MANAGER')"))
                        .requestMatchers("/shoppingCart/**").hasRole("USER")
                        .requestMatchers("/foodOrders/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
