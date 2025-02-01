package com.example.myapp.member.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.example.myapp.member.dao.IMemberRepository;
import com.example.myapp.member.model.Member;
import com.example.myapp.util.RedisUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements IMemberService {

	private final JavaMailSender javaMailSender;
	private final RedisUtil redisUtil;
	private static final String senderEmail = "neighclova@gmail.com";
	
	@Autowired
	IMemberRepository memberDao;
	
	@Override
	public void insertMember(Member member) {
		memberDao.insertMember(member);
	}

	@Override
	public Member selectMember(String userid) {
		return memberDao.selectMember(userid);
	}

	@Override
	public List<Member> selectAllMembers() {
		return memberDao.selectAllMembers();
	}

	@Override
	public void updateMember(Member member) {
		memberDao.updateMember(member);
	}

	@Override
	public void deleteMember(Member member) {
		memberDao.deleteMember(member);
	}

	@Override
	public String getPassword(String userid) {
		return memberDao.getPassword(userid);
	}
	
    private String createCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // 이메일 내용 초기화
    private String setContext(String code) {
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        context.setVariable("code", code);

        templateResolver.setPrefix("templates/member/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mail", context);
    }

    // 이메일 폼 생성
    private MimeMessage createEmailForm(String email) throws MessagingException {
        String authCode = createCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[Metanet] 회원가입 이메일 인증번호입니다.");
        message.setFrom(senderEmail);
        message.setText(setContext(authCode), "utf-8", "html");

        // Redis 에 해당 인증코드 인증 시간 설정
        redisUtil.setDataExpire(email, authCode, 60 * 30L);

        return message;
    }
    
	// 인증코드 이메일 발송
	@Override
	public void sendEmail(String email) throws MessagingException {
		log.info(email);
		if (redisUtil.existData(email)) {
	        redisUtil.deleteData(email);
	    }
	    // 이메일 폼 생성
	    MimeMessage emailForm = createEmailForm(email);
	    // 이메일 발송
	    javaMailSender.send(emailForm);	
	}

	// 코드 검증
	@Override
	public boolean verifyEmailCode(String email, String code) {
		String codeFoundByEmail = redisUtil.getData(email);
        log.info("code found by email: " + codeFoundByEmail);
        if (codeFoundByEmail == null) {
            return false;
        }
        return codeFoundByEmail.equals(code);
	}

}