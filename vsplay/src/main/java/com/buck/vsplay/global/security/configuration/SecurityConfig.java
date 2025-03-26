package com.buck.vsplay.global.security.configuration;

import com.buck.vsplay.global.security.filter.JwtVerificationFilter;
import com.buck.vsplay.global.security.filter.JwtAuthenticationFilter;
import com.buck.vsplay.global.security.handler.VsPlayAuthenticationFailureHandler;
import com.buck.vsplay.global.security.handler.VsPlayAuthenticationSuccessHandler;
import com.buck.vsplay.global.security.handler.VsPlayLogoutHandler;
import com.buck.vsplay.global.security.handler.VsPlayLogoutSuccessHandler;
import com.buck.vsplay.global.security.jwt.JwtAuthenticationEntryPoint;
import com.buck.vsplay.global.security.jwt.JwtService;
import com.buck.vsplay.global.security.jwt.exception.JwtExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final VsPlayLogoutHandler vsPlayLogoutHandler;
    private final VsPlayLogoutSuccessHandler vsPlayLogoutSuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {
                    // 🔹 인증이 필요 없는 경로 적용 (PUBLIC_ENDPOINTS)
                    PublicPaths.PUBLIC_ENDPOINTS.forEach((url, methods) ->
                            methods.forEach(method -> auth.requestMatchers(method, url).permitAll()));

                    auth.anyRequest().authenticated(); // 나머지 경로 인증
                })
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // ✅ 인증 실패 시 401 반환
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                        .logout( logout -> logout
                                .logoutUrl("/member/logout")
                                .addLogoutHandler(vsPlayLogoutHandler)
                                .logoutSuccessHandler(vsPlayLogoutSuccessHandler)
                        );
        addCustomFilters(httpSecurity);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
    ) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8081");
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
                .addFilterBefore(jwtVerificationFilter, JwtAuthenticationFilter.class);
    }


    private JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/member/login");
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(new VsPlayAuthenticationSuccessHandler(jwtService));
        jwtAuthenticationFilter.setAuthenticationFailureHandler(new VsPlayAuthenticationFailureHandler());

        return jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
