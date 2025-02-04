package com.example.myapp.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.common.response.ResponseDto;
import com.example.myapp.member.model.Member;
import com.example.myapp.member.service.IMemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthRestController {
	
	@Autowired
	IMemberService memberService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	

	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<ResponseDto> join(@RequestBody Member member) {			
		try {			
			if(member.getRole().equals("teacher")) {
				if(member.getBank().isEmpty()) {
					return ResponseDto.nullInputValue();
				}
			} 
			String encodedPw = passwordEncoder.encode(member.getPassword());				
			member.setPassword(encodedPw);			
			memberService.insertMember(member);							
		} catch (DuplicateKeyException e) {
			member.setId(null);
			return ResponseDto.duplicatedId();
		}
		return ResponseDto.success();
	}

	
//	@PostMapping("/login")
//	public ResponseEntity<LoginResponseDto> login(@RequestBody Member member){
//		 return ResponseEntity.ok(memberService.login(member));
//	}

//	@PostMapping("/login")
//	public String login(@RequestBody Map<String, String> user) {
//		log.info(user.toString());
//		Member member = memberService.selectMember(user.get("userid"));
//		if (member == null) {
//			throw new IllegalArgumentException("사용자가 없습니다.");
//		}
//		if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
//			throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//		}
//		return jwtTokenProvider.generateToken(member);
//	}
//
//	@GetMapping("/test_jwt")
//	public String testJwt(HttpServletRequest request) {
//		String token = jwtTokenProvider.resolveToken(request);
//		log.info("token {}", token);
//		Authentication auth = jwtTokenProvider.getAuthentication(token);
//		log.info("principal {}, name {}, authorities {}", auth.getPrincipal(), auth.getName(), auth.getAuthorities());
//		log.info("isValid {}", jwtTokenProvider.validateToken(token));
//		return jwtTokenProvider.getUserId(token);
//	}
}