package com.example.myapp.aop;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
	
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final IAopRepository aopRepository;
	
	@Around("execution(* com.example.myapp..*Service.*(..))")
	public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String methodName = joinPoint.getSignature().getName();
        String url = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        String clientIp = request.getRemoteAddr();
        LocalDateTime requestTime = LocalDateTime.now();
        
        log.info("[[[AOP-before log]]]-{}: Request to URL '{}' with HTTP Method '{}' from IP '{}'", methodName, url, httpMethod, clientIp);
        
        Object result;
        LocalDateTime responseTime;
        try {
            result = joinPoint.proceed();
            responseTime = LocalDateTime.now();
            log.info("[[[AOP-after log]]]-{}: Method executed successfully", methodName);
        } catch (Throwable throwable) {
            responseTime = LocalDateTime.now();
            log.error("[[[AOP-exception log]]]-{}: Exception occurred: {}", methodName, throwable.getMessage());
            throw throwable;
        }
        
        Log logEntry = new Log();
        logEntry.setRequestUrl(url);
        logEntry.setRequestMethod(httpMethod);
        logEntry.setClientIp(clientIp);
        logEntry.setRequestTime(requestTime);
        logEntry.setResponseTime(responseTime);
        logEntry.setResponseStatus(response.getStatus());

        log.info("Log : {}", logEntry);
        aopRepository.insertLog(logEntry);

        return result;
	}
}
