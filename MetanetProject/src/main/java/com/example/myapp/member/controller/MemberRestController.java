package com.example.myapp.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.jwt.JwtTokenProvider;
import com.example.myapp.member.model.Member;
import com.example.myapp.member.service.IMemberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberRestController {
	
	@Autowired
	IMemberService memberService;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	

	// 회원가입
	@PostMapping("/join")
	public String join(@RequestBody Member member) {		
		try {
			// 비밀번호 확인 여부 체크
			if (!member.getPassword().equals(member.getPassword2())) {
				return "비밀번호 확인을 다시 해주세요";
			} else {
				String encodedPw = passwordEncoder.encode(member.getPassword());				
				member.setPassword(encodedPw);
				member.setRole("user");
				memberService.insertMember(member);
			}
		} catch (DuplicateKeyException e) {
			member.setId(null);
			return "이미 있는 회원입니다.";
		}

		return "회원가입 되었습니다.";
	}



	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> user) {
		log.info(user.toString());
		Member member = memberService.selectMember(user.get("userid"));
		if (member == null) {
			throw new IllegalArgumentException("사용자가 없습니다.");
		}
		if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
			throw new IllegalArgumentException("잘못된 비밀번호입니다.");
		}
		return jwtTokenProvider.generateToken(member);
	}

	@GetMapping("/test_jwt")
	public String testJwt(HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		log.info("token {}", token);
		Authentication auth = jwtTokenProvider.getAuthentication(token);
		log.info("principal {}, name {}, authorities {}", auth.getPrincipal(), auth.getName(), auth.getAuthorities());
		log.info("isValid {}", jwtTokenProvider.validateToken(token));
		return jwtTokenProvider.getUserId(token);
	}
}