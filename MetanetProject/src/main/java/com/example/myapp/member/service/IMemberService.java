package com.example.myapp.member.service;

import java.util.List;
import java.util.Optional;

import com.example.myapp.common.response.ResponseDto;
import com.example.myapp.member.model.Member;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface IMemberService {
	// 인증 코드 이메일 발송
	ResponseEntity<ResponseDto> sendEmail(String email) throws MessagingException;
	
	// 코드 검증
	ResponseEntity<ResponseDto> verifyEmailCode(String email, String code);
	
	//회원가입
	void insertMember(Member member) ;
	
	//로그인
	//LoginResponseDto login(Member member);
	
	//id로 조회하기
	//<Member> findById(String id);
	
	Member selectMember(String userid);
	List<Member> selectAllMembers();
	void updateMember(Member member);
	void deleteMember(Member member);
	String getPassword(String userid);
}