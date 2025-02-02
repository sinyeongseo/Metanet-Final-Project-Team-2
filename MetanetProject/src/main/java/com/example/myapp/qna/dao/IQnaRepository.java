package com.example.myapp.qna.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.myapp.qna.model.Question;

@Repository
@Mapper
public interface IQnaRepository {
	void insertQuestion(Question question);
}
