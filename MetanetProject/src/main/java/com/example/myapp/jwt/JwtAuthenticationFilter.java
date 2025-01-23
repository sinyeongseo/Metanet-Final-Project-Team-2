package com.example.myapp.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 헤더에서 JWT를 받아음.
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		// 유효한 토큰인지 확인.
		if (token != null && jwtTokenProvider.validateToken(token)) {
			// 토큰이 유효하면 토큰으로부터 사용자 정보를 받아옴
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			// SecurityContext에 Authentication 객체를 저장.
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
}