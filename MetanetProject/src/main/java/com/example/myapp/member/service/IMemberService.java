package com.example.myapp.member.service;

import java.util.List;

import com.example.myapp.member.model.Member;

import jakarta.mail.MessagingException;

public interface IMemberService {
	// 인증 코드 이메일 발송
	void sendEmail(String email) throws MessagingException;
	
	// 코드 검증
	boolean verifyEmailCode(String email, String code);
	
	void insertMember(Member member) ;
	Member selectMember(String userid);
	List<Member> selectAllMembers();
	void updateMember(Member member);
	void deleteMember(Member member);
	String getPassword(String userid);
}