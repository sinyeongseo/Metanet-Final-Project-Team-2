package com.example.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.myapp.jwt.JwtAuthenticationFilter;
import com.example.myapp.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//
//    private final CustomUserDetailsService customUserDetailsService;
      private final JwtTokenProvider jwtTokenProvider;
//    
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = 
//            http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder
//            .userDetailsService(customUserDetailsService) // 커스텀 UserDetailsService 사용
//            .passwordEncoder(passwordEncoder()); // 비밀번호 인코더 사용
//        return authenticationManagerBuilder.build();
//    }
//    
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable() // 서버에 인증정보를 저장하지 않기 때문에(stateless, rest api) csrf를 추가할 필요가 없다.         
//            .httpBasic().disable() // 기본 인증 로그인 사용하지 않음. (rest api)
//            .cors()
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // stateless: 세션을 사용하지 않음.
//
//            // request permission
//            .and()
//            .authorizeRequests()
//            .antMatchers("/", "/favicon.ico", "/auth/**").permitAll() // 인증 없이 접근 허용
//            .antMatchers("/admin/**").hasRole("ADMIN") // "ROLE_ADMIN" 권한이 있어야 접근 가능
//            .anyRequest().authenticated() // 나머지 요청은 모두 인증을 요구
//
//            // exception handling
//            .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 오류 처리
//            .accessDeniedHandler(new CustomAccessDeniedHandler()) // 권한 오류 처리
//
//            // jwt filter -> 인증 정보 필터링 전에(filterBefore) 필터
//            .and()
//            .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가
//
//        return http.build();
//    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    // Rest API이기 때문에 csrf 보안 사용 X
	    http.csrf((csrfConfig) -> csrfConfig.disable());
	    // JWT를 사용하기 때문에 세션 사용 비활성
	    http.sessionManagement((session) -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    // 인가 규칙 설정
	    http.authorizeHttpRequests(auth -> auth
	            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
	            .requestMatchers("/auth/**").permitAll()
	            .requestMatchers("/board/test").hasAnyRole("User", "Teacher")
	            .anyRequest().authenticated()  // 모든 요청은 인증이 필요
	    );

	    // JWT 인증을 위해 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
	    http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

	    return http.build();  // 필터 체인 빌드
	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화에 BCryptPasswordEncoder 사용
    }
}
