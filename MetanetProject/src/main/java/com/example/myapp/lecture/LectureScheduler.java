package com.example.myapp.lecture;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.myapp.lecture.dao.ILectureRepository;
import com.example.myapp.lecture.model.LectureReminderDto;
import com.example.myapp.lecture.model.LectureScheduled;
import com.example.myapp.member.service.IMemberService;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LectureScheduler {

	@Autowired
    ILectureRepository lectureRepository;
	
	@Autowired
    IMemberService emailService;
    
	@Scheduled(cron = "0 00 00 * * *")// 자정에 강의 일정 보내기
	public void sendDailyLectureSchedule() {
	    List<LectureScheduled> schedules = lectureRepository.findTodayLectures();

	    // 비동기 이메일 전송
	    for (LectureScheduled scheduled : schedules) {
	        try {
	        	log.info(scheduled.getEmail() + " and " + scheduled.getSchedule());

	        	emailService.sendEmail("lecture_schedule", scheduled.getEmail(), scheduled.getSchedule());
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    }
	}
	

	@Scheduled(cron = "0 */5 * * * ?") // 5분마다 실행
    public void sendLectureReminderEmails() {    
        List<LectureReminderDto> lectures = lectureRepository.getLecturesStartingIn30Minutes();
        for (LectureReminderDto lecture : lectures) {
        	try {
	            emailService.sendEmail("lecture_reminder", lecture.getEmail(), lecture);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
        }
    }
	
}

