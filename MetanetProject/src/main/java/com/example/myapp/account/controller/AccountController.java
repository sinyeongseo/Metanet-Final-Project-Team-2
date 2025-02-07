package com.example.myapp.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.myapp.account.service.IAccountService;
import com.example.myapp.common.response.ResponseDto;
import com.example.myapp.util.GetAuthenUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	IAccountService accountService;

	//수강 강의 목록 조회 - 신영서
	@GetMapping("/lecture")
	public ResponseEntity<ResponseDto> getMyLecture() {
		String user = GetAuthenUser.getAuthenUser();
		// 인증되지 않은 경우는 바로 처리
		if (user == null) {
			return ResponseDto.noAuthentication();
		}

		return accountService.getLecture(user);
	}

	// 내 관심 분야 등록 - 신영서
	@PostMapping("/category")
	public ResponseEntity<ResponseDto> insertCategory(@RequestParam String tags) {
		String user = GetAuthenUser.getAuthenUser();
		
		if (user == null) {
			return ResponseDto.noAuthentication();
		}		
		return accountService.insertCategory(tags, user);
	}
	
	// 프로필 수정 - 신영서
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateProfile(@RequestParam("files") MultipartFile files){
		String user = GetAuthenUser.getAuthenUser();
		
		if (user == null) {
			return ResponseDto.noAuthentication();
		}
		
		return accountService.updateProfile(user, files);
	}
	
	
	// 프로필 조회 - 신영서	
	@GetMapping
	public ResponseEntity<ResponseDto> getMypage(){	
		
		String user = GetAuthenUser.getAuthenUser();
		
		if (user == null) {
			return ResponseDto.noAuthentication();
		}
						
		return accountService.getMyPage(user);
	}
	
	
	//대시보드 조회 - 내학습률 -모두
//	@GetMapping("/myTech")
//	public ResponseEntity<ResponseDto> getMytech(){
//		
//		String user = GetAuthenUser.getAuthenUser();
//		
//		if (user == null) {
//			return ResponseDto.noAuthentication();
//		}
//		
//		
//		return accountService.getMyTech(user);
//	}
//	
//	
//	
//	//대시보드 조회 - 강의 -Teacher, Admin
//	@GetMapping("/lecture-dashboard")
//	public ResponseEntity<ResponseDto> getLectureDashBoard(){
//		
//		String user = GetAuthenUser.getAuthenUser();
//		
//		if (user == null) {
//			return ResponseDto.noAuthentication();
//		}
//		
//		
//		return accountService.getLectureDashBoard(user);
//	}
	
	
	
	//결제 내역 조회 - 모두 
	 
	
	
	
	
}	
