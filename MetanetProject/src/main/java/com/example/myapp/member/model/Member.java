package com.example.myapp.member.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Member {
	
    private String id;
	private String name;
	private String password;
	private String password2;
	private String phone;
	private String email;	
    private String role;
    private int deleted;
    private String car;
    private int alarm_status;
   
}