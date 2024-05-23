package net.wallet.main.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigaration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception{
        http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/auth/**","upload.html","/reg","/uploads","/swagger-ui/**",
        "/swagger-ui.html","/v3/api-docs/**","/*.jpg","/*.png","/*.jpeg")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors().configurationSource(request -> {
            CorsConfiguration cors = new CorsConfiguration();
            cors.addAllowedOrigin("http://localhost:3000"); // Specify your allowed origins (Please customized it in production)
            cors.addAllowedOrigin("http://localhost:5173"); 
            cors.addAllowedOrigin("http://127.0.0.1:5173"); //VUE3 LOCALHOST
            cors.addAllowedOrigin("http://localhost:5174"); 
            cors.addAllowedOrigin("http://127.0.0.1:5174");
            cors.addAllowedOrigin("https://alifpay-web.vercel.app");
            cors.addAllowedHeader("*"); // Specify your allowed headers
            cors.addAllowedMethod(HttpMethod.GET);
            cors.addAllowedMethod(HttpMethod.POST);
            cors.addAllowedMethod(HttpMethod.PATCH);
            cors.addAllowedMethod(HttpMethod.PUT);
            cors.addAllowedMethod(HttpMethod.DELETE);
            cors.addAllowedMethod(HttpMethod.OPTIONS);
            return cors;
        })
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
}
