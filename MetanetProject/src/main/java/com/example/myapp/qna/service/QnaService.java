package com.example.myapp.qna.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.myapp.qna.model.Answer;
import com.example.myapp.qna.model.Notification;
import com.example.myapp.qna.model.Question;

@Service
public class QnaService implements IQnaService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Override
	public void registerQuestion(Question question) {
		//질문 DB에 저장
		
		//알림 생성
		Notification notification = new Notification();
		notification.setMessage(question.getLecture() 
				+ "강의에 질문이 등록되었습니다.");
		notification.setTimestamp(LocalDateTime.now());
		
		//강의자에게 알림 전송
		messagingTemplate.convertAndSendToUser(
				question.getTeacher(),
				"queue/notifications",
				notification
		);
	}

	@Override
	public void registerAnswer(Answer answer) {
		//답변 DB 저장
		
		//알림 생성
		Notification notification = new Notification();
		notification.setMessage(answer.getWriter()
				+ "님이 답변을 등록했습니다.");
		notification.setTimestamp(LocalDateTime.now());
		
		//학생에게 알림 전송
		messagingTemplate.convertAndSendToUser(
				answer.getQuestionWriter(),
				"queue/notifications",
				notification
		);
	}

}
