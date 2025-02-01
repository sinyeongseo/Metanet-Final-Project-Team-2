package com.example.myapp.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.member.model.Email;
import com.example.myapp.member.service.IMemberService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
	
	@Autowired
	IMemberService memberService;

	// 인증코드 메일 발송
	@PostMapping("/send")
	public String mailSend(@RequestBody Email email) throws MessagingException {
		memberService.sendEmail(email.getEmail());
		log.info("EmailController.verify()");
		return "인증코드가 발송되었습니다.";
	}
	
	// 인증코드 인증
	@PostMapping("/verify")
	public String verify(@RequestBody Email email) {
		boolean isVerify = memberService.verifyEmailCode(email.getEmail(), email.getVerifyCode());
		return isVerify ? "인증이 완료되었습니다." : "인증 실패하셨습니다.";
	}
}