package com.example.myapp.qna.service;

import com.example.myapp.qna.model.Answer;
import com.example.myapp.qna.model.Question;

public interface IQnaService {
	void registerQuestion(Question question);
	void registerAnswer(Answer answer);
}
