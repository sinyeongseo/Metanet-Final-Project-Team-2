package com.example.myapp.member.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Member {
	
	private String profile;
    private String id;
	private String password;
	private String password2;
	private String phone;
	private String email;	
	private Date birth;
	private String name;
    private String role;
    private String bank;
    private int deleted;        
   
}