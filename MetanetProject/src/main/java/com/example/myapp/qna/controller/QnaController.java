package com.example.myapp.qna.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.qna.model.Answer;
import com.example.myapp.qna.model.Question;
import com.example.myapp.qna.service.IQnaService;

@RestController
@RequestMapping("/lectures")
public class QnaController {

	@Autowired
	private IQnaService qnaService;
	
	@PostMapping("/{lectureId}/questions")
	public ResponseEntity<String> registerQuestion(@RequestBody Question question) {
		try {
			qnaService.registerQuestion(question);
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("질문 등록 실패 : " + e.getMessage());
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("질문 등록 및 알림 성공");
	}
	
	@PostMapping("/{lectureId}/questions/{questionId}/answers")
	public ResponseEntity<String> registerAnswer(@RequestBody Answer answer) {
		try {
			qnaService.registerAnswer(answer);
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("질문 등록 실패 : " + e.getMessage());
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("질문 등록 및 알림 성공");
	}
}
