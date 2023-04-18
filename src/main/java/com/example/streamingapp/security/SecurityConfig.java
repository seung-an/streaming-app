package com.example.streamingapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // .httpBasic().disable().csrf().disable(): rest api이므로 basic auth 및 csrf 보안을 사용하지 않는다는 설정
                .httpBasic().disable().csrf().disable()

                // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS): JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()

                // 정적자원에 대해서 요청을 허가한다는 설정
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                .antMatchers("/api/**").authenticated()

                // .antMatchers("호출 URL").permitAll(): 해당 API에 대해서는 모든 요청을 허가한다는 설정
                // .antMatchers("호출 URL").authenticated(): 해당 API 에 대해 인증을 필요로 한다는 설정
                // .antMatchers("호출 URL").hasRole("권한명"): 특정 권한이 있어야 요청할 수 있다는 설정
                // .antMatchers("호출 URL").hasAnyRole("권한명1", "권한명2"): 권한1 또는 권한2 이 있어야 요청할 수 있다는 설정
                // .anyRequest().authenticated(): 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                // addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                // : JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행하겠다는 설정
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
