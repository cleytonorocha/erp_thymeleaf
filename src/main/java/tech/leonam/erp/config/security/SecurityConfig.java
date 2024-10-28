package tech.leonam.erp.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtAuthFilter jwtAuthFilter;

        @Bean
        MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
                return new MvcRequestMatcher.Builder(introspector);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(final HttpSecurity http, MvcRequestMatcher.Builder mvc)
                        throws Exception {
                return http
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.disable())
                                .formLogin(f -> {
                                        f.loginPage("/login").permitAll();
                                        f.failureUrl("/login/error");
                                        f.loginProcessingUrl("/auth/login");
                                        f.successForwardUrl("/");
                                        f.defaultSuccessUrl("/", true);
                                }).logout(LogoutConfigurer::permitAll)

                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/login", "/register", "login/error").permitAll()
                                                .requestMatchers(mvc.pattern(HttpMethod.POST, "/auth/login"))
                                                .permitAll()
                                                .requestMatchers(mvc.pattern(HttpMethod.POST, "/auth/register"))
                                                .permitAll()
                                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"),
                                                                AntPathRequestMatcher.antMatcher("/static/**"))
                                                .permitAll()
                                                .anyRequest().authenticated())

                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .headers(headers -> headers.frameOptions(f -> f.disable()))
                                .build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
