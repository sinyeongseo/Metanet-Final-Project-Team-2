package com.example.myapp.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.common.response.ResponseDto;
import com.example.myapp.jwt.SecurityUtil;
import com.example.myapp.jwt.model.JwtToken;
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
			if (member.getRole().equals("teacher")) {
				if (member.getBank().isEmpty()) {
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

	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody Member member) {
	    JwtToken jwtToken;

	    jwtToken = memberService.loginService(member);

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer " + jwtToken.getAccessToken());
	            
	    return ResponseEntity.ok()
	            .headers(headers)
	            .build();  
	}


}