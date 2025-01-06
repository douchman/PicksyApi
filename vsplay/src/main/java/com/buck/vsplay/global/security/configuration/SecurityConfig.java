package com.buck.vsplay.global.security.configuration;

import com.buck.vsplay.global.security.filter.JwtVerificationFilter;
import com.buck.vsplay.global.security.filter.JwtAuthenticationFilter;
import com.buck.vsplay.global.security.handler.VsPlayAuthenticationFailureHandler;
import com.buck.vsplay.global.security.handler.VsPlayAuthenticationSuccessHandler;
import com.buck.vsplay.global.security.jwt.JwtService;
import com.buck.vsplay.global.security.jwt.exception.JwtExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;
    private final JwtExceptionHandler jwtExceptionHandler;

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/vstopic").hasRole("GENERAL")
                        .requestMatchers(HttpMethod.POST, "/member").permitAll()
                        .requestMatchers("/member/login").permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        addCustomFilters(httpSecurity);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
    ) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    public void addCustomFilters(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class));

        JwtAuthenticationFilter jwtAuthenticationFilter = jwtAuthenticationFilter(authenticationManager);
        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtService, jwtExceptionHandler);

        httpSecurity
                .addFilter(jwtAuthenticationFilter)
                .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
    }


    private JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/member/login");
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(new VsPlayAuthenticationSuccessHandler());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(new VsPlayAuthenticationFailureHandler());

        return jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
