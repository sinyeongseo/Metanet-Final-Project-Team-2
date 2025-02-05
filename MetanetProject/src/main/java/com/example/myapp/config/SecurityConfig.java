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

      private final JwtTokenProvider jwtTokenProvider;

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    // Rest API이기 때문에 csrf 보안 사용 X
	    http.csrf((csrfConfig) -> csrfConfig.disable());
	    http.cors(cors -> cors.disable()); //웹소켓땜에 개발할 때 잠깐 꺼둠
	    // JWT를 사용하기 때문에 세션 사용 비활성
	    http.sessionManagement((session) -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    // 인가 규칙 설정
	    http.authorizeHttpRequests(auth -> auth
	            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
	            .requestMatchers("/auth/**").permitAll()
	            .requestMatchers("/lectures/**").hasAnyRole("User", "Teacher")
	            .requestMatchers("/ws/**").permitAll()
	            .requestMatchers("/user/**").permitAll()
	            .requestMatchers("/topic/**", "/queue/**").permitAll()
	            //.requestMatchers("/board/test").hasAnyRole("User", "Teacher")
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
