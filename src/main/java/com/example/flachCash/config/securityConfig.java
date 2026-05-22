package com.example.flachCash.config;


import com.example.flachCash.domain.User;
import com.example.flachCash.domain.UserAccount;
import com.example.flachCash.repository.UserRepository;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
    //to configure H2 see project spring-security method2

    //list of vue authorized
    private static final String[] AUTH_WHITELIST ={
            "/css/**",
            "/js/**",
            "/register",
            "/img/**",
            "/h2-console/**",
            "/home",
            "/login"
    };

    //Authorisation of vue
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                //Permission of the http or else  → white blank or 403
                .authorizeHttpRequests(req -> req
                        //authorization forward( intern redirection) and error (request of error vue)(404 ET 500)
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/profile/**", "/transaction/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(
                        form ->form
                                .loginPage("/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/home-profile",true)
                                .permitAll()
                ).logout(logout -> logout //on force la session à se deconnecter
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                );

        return httpSecurity.build();

    }


    //Creation of USER
    @Bean
    public CommandLineRunner initDatabase(){
        return args -> {


            if (userRepository.findUserByEmail("user@example.com").isEmpty()) {
                UserAccount account = UserAccount.builder()
                        .balance(0.0)
                        .build();
                User user = User.builder()
                        .firstName("user")
                        .lastName("Normal")
                        .email("user@example.com")
                        .password(passwordEncoder().encode("Awatef&é12"))
                        .account(account)
                        .role(USER)
                        .build();
                userRepository.save(user);
            }

            if (userRepository.findUserByEmail("admin@example.com").isEmpty()) {
                UserAccount account = UserAccount.builder()
                        .balance(100.0)
                        .build();

                User admin = User.builder()
                        .firstName("admin")
                        .lastName("Super")
                        .email("admin@example.com")
                        .password(passwordEncoder().encode("Awatefà&01"))
                        .account(account)
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
    //Encryption of password
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
