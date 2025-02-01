package com.example.myapp.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.member.model.Member;

@Repository
@Mapper
public interface IMemberRepository {
	
	// 인증코드 발송한 이메일 주소 조회???
	//void
	
	void insertMember(Member member) ;
	Member selectMember(String userid);
	List<Member> selectAllMembers();
	void updateMember(Member member);
	void deleteMember(Member member);
	String getPassword(String userid);
}