package com.saurabh.securenotesapi.security;

import com.saurabh.securenotesapi.model.AppRole;
import com.saurabh.securenotesapi.model.Role;
import com.saurabh.securenotesapi.model.User;
import com.saurabh.securenotesapi.repository.RoleRepository;
import com.saurabh.securenotesapi.repository.UserRepository;
import com.saurabh.securenotesapi.security.jwt.AuthEntryPointJwt;
import com.saurabh.securenotesapi.security.jwt.AuthTokenFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.time.LocalDate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;

    public SecurityConfig(AuthEntryPointJwt unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf ->
                        csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .ignoringRequestMatchers("/api/auth/public/**"))
//                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/auth/public/**").permitAll()
                                .requestMatchers("/api/csrf-token").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler))
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com",
                        passwordEncoder.encode("password1"));
                user1.setAccountNonLocked(false);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRole(userRole);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com",
                        passwordEncoder.encode("adminPass"));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        };
    }
}
