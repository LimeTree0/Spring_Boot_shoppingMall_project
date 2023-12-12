package springshop.springshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import springshop.springshop.service.MemberService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private MemberService memberService;

    public SecurityConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    // embedded h2-console access permission
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers("/h2-console/**", "/favicon.ico",
                        "/css/**", "/js/**", "/img/**"));
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                formLogin((login) -> login
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .failureUrl("/members/login/error"))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/"));

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.exceptionHandling((httpSecurity) ->
                httpSecurity.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
