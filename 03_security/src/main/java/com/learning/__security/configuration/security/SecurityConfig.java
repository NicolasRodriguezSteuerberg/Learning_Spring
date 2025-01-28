package com.learning.__security.configuration.security;

import com.learning.__security.service.implementation.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // controller 1
//    @Bean
//    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(e -> e.disable())
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(e -> e
//                        // configurar los endpoints publicos
//                        .requestMatchers("/auth/hello").permitAll()
//                        // configurar endpoints 'privados'
//                        .requestMatchers(HttpMethod.GET, "/auth/hello-secured").hasAuthority("CREATE")
//                        // configurar resto endpoints
//                        .anyRequest().authenticated()
//                        //.anyRequest().denyAll()
//                )
//                .build();
//    }

    /* controller 2
    @Bean
    public SecurityFilterChain securityFilterChain2 (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(e -> e.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
     */

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(e -> e.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(e -> e
                                .requestMatchers(HttpMethod.GET, "/auth3/get").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth3/post").hasAnyAuthority("CREATE", "READ")
                                .requestMatchers(HttpMethod.PATCH, "/auth3/patch").hasAnyRole("ADMIN", "DEVELOPER")
                                .requestMatchers(HttpMethod.DELETE, "/auth3/delete").hasRole("ADMIN")
                                // configurar resto endpoints
                                .anyRequest().authenticated()
                        //.anyRequest().denyAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        // provider.setUserDetailsService(userDetailsServiceList()); <-- inmemory
        provider.setUserDetailsService(userDetailService);
        return provider;
    }


    // en memoria (test rapido)
    /*
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User
                .withUsername("nico")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ", "CREATE")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
     */
    /*@Bean
    public UserDetailsService userDetailsServiceList() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(
                User
                        .withUsername("nico")
                        .password("1234")
                        .roles("ADMIN")
                        .authorities("READ", "CREATE")
                        .build()
        );
        userDetailsList.add(
                User
                        .withUsername("irene")
                        .password("1234")
                        .roles("USER")
                        .authorities("READ")
                        .build()
        );
        return new InMemoryUserDetailsManager(userDetailsList);
    }
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        // solo para pruebas
        //return NoOpPasswordEncoder.getInstance();
        //return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        return new BCryptPasswordEncoder();
    }

}
