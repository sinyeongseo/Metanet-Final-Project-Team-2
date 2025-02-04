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
import com.example.myapp.jwt.model.JwtToken;
import com.example.myapp.member.model.Member;
import com.example.myapp.member.service.IMemberService;
import com.example.myapp.util.RegexUtil;

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
	        // 전화번호 검증
	        RegexUtil regexUtil = new RegexUtil();
	        
	        if (member.getPhone() != null && !regexUtil.telNumber(member.getPhone())) {
	           return ResponseEntity.badRequest().body(new ResponseDto("REGEX_ERROR", "Phone value not match Regex"));
	        }

	        // 이메일 검증
	        if (member.getEmail() != null && !regexUtil.checkEmail(member.getEmail())) {
	        	return ResponseEntity.badRequest().body(new ResponseDto("REGEX_ERROR", "Email value not match Regex"));
	        }

	        // 비밀번호 검증
	        if (member.getPassword() != null && !regexUtil.checkPassword(member.getPassword())) {
	        	return ResponseEntity.badRequest().body(new ResponseDto("REGEX_ERROR", "Password value not match Regex"));
	        }

	        // 역할이 teacher일 경우 은행 계좌 정보 검증
	        if (member.getRole().equals("teacher")) {
	            if (member.getBank().isEmpty()) {
	                return ResponseDto.nullInputValue(); // 은행 계좌 입력 값 없음
	            }
	        }

	        // 비밀번호 암호화
	        String encodedPw = passwordEncoder.encode(member.getPassword());
	        member.setPassword(encodedPw);

	        // 회원 정보 저장
	        memberService.insertMember(member);

	    } catch (DuplicateKeyException e) {
	        member.setId(null);
	        return ResponseDto.duplicatedId(); // ID 중복 오류
	    }

	    return ResponseDto.success(); // 회원가입 성공
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