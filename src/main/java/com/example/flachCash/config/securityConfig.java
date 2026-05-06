package com.example.flachCash.config;


import com.example.flachCash.domain.User;
import com.example.flachCash.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.flachCash.domain.Role.ADMIN;
import static com.example.flachCash.domain.Role.USER;

//tous les beans sont pour spring boot reconnaissance
@Configuration
@EnableWebSecurity //activation de web security
@RequiredArgsConstructor // pour éviter les autowired
public class securityConfig {

    private final UserRepository userRepository;
    //Add FilterChain
    //Pour configuration H2 il faut voir methode2 et methodeH2

    //definition des lien à authoriszer
    private static final String[] AUTH_WHITELIST ={
            "/css/**",
            "/js/**",
            "/login",
            "/register",
            "/img/**",
            "/h2-console/**",


    };

    //autorisation des vues
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // ignor cette endpoint te l'exclu de la sécutité
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .authorizeHttpRequests(req -> req
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                // on peut rajouter oa
                .formLogin( //configuration de l'authentification et de la redirection
                        form ->form
                                .loginPage("/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/profile",true)
                                .permitAll()
                );

        return httpSecurity.build();

    }

    //creaction des users
    @Bean
    public CommandLineRunner initDatabase(){
        return args -> {

            if (userRepository.findUserByEmail("user@example.com").isEmpty()) {
                User user = User.builder()
                        .firstName("user")
                        .lastName("Normal")
                        .email("user@example.com")
                        .password(passwordEncoder().encode("awatefé&à&_("))
                        .role(USER)
                        .build();
                userRepository.save(user);
            }

            if (userRepository.findUserByEmail("admin@example.com").isEmpty()) {
                User admin = User.builder()
                        .firstName("admin")
                        .lastName("Super")
                        .email("admin@example.com")
                        .password(passwordEncoder().encode("password"))
                        .role(ADMIN)
                        .build();
                userRepository.save(admin);
            }

        };
    }
    // authentification via email
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email" +username+ " not found"));


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
