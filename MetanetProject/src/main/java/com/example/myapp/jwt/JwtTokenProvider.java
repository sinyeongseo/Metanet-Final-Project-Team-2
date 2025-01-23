package com.example.myapp.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.example.myapp.member.model.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT를 생성하고 검증하는 서비스를 제공
 * @author JinKyoung Heo
 * @version 1.0
 */
@Slf4j
@Component
public class JwtTokenProvider {

	/**
	 * 토큰 암호화에 사용할 키
	 */
	private static SecretKey key = Jwts.SIG.HS256.key().build();

	/**
	 * 토큰 유효기간, 30분, 단위 밀리초
	 */
	private long tokenValidTime = 30 * 60 * 1000L;

	@Autowired
	UserDetailsService userDetailsService;

	/**
	 * 토큰을 만들어 반환
	 * @param member 사용자 정보를 저장한 객체, 클래임에 사용자 정보를 저장하기 위해 필요
	 * @return 생성된 토큰
	 */
	public String generateToken(Member member) {
		long now = System.currentTimeMillis();
		Claims claims = Jwts.claims()
				.subject(member.getUserid()) 	// sub
				.issuer(member.getName()) 		// iss
				.issuedAt(new Date(now)) 		// iat
				.expiration(new Date(now + tokenValidTime)) // exp
				.add("roles", member.getRole()) // roles
				.build();
		return Jwts.builder()
				.claims(claims) 
				.signWith(key)  // 암호화에 사용할 키 설정
				.compact();
	}

	/**
	 * Request의 Header에서 token 값을 가져옴 "X-AUTH-TOKEN" : "TOKEN값"
	 * @param request 요청 객체
	 * @return 토큰
	 */
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("X-AUTH-TOKEN");
	}

	/**
	 * 토큰에서 회원 정보 추출
	 * @param token 토큰
	 * @return 토큰에서 사용자 아이디를 추출해서 반환
	 */
	public String getUserId(String token) {
		log.info(token);
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject(); // generateToken()에서 subject에 userid를 담았었음
	}

	/**
	 * JWT 토큰에서 인증 정보 조회
	 * @param token 토큰
	 * @return 인증 정보 Authentication 객체
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
		log.info(userDetails.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	/**
	 * 토큰의 유효성과 만료 일자 확인
	 * @param token 토큰
	 * @return 토큰이 유효한지 확인, 유효하면 true 반환
	 */
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token);
			return !claims.getPayload().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}