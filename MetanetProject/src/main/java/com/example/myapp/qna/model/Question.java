package com.example.myapp.qna.model;

import lombok.Data;

@Data
public class Question {
	private String lecture;
	private String writer;
	private String title;
	private String content;
	private String teacher;
}
