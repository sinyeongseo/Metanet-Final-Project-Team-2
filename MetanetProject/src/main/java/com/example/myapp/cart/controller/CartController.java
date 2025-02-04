package com.example.myapp.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.cart.model.CartDeleteRequest;
import com.example.myapp.cart.service.ICartService;
import com.example.myapp.common.response.ResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	ICartService cartService;

	// 장바구니 조회
	@GetMapping
	public ResponseEntity<ResponseDto> getCarts() {
		String memberId = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
        try {
            if (authentication != null) {
                // 현재 인증된 사용자 정보
                memberId = authentication.getName();
                log.info(memberId);
            }

            if (memberId == null)
                return ResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return ResponseDto.databaseError();
        }

        ResponseEntity<ResponseDto> response = cartService.getCarts(memberId);
        return response;
    }
	
	// 장바구니 추가
	@PostMapping
	public ResponseEntity<ResponseDto> addCart(@RequestParam String lectureId) {
		String memberId = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info(lectureId);
        try {
            if (authentication != null) {
                // 현재 인증된 사용자 정보
                memberId = authentication.getName();
            }

            if (memberId == null)
                return ResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return ResponseDto.databaseError();
        }

        ResponseEntity<ResponseDto> response = cartService.addCart(memberId, lectureId);
        return response;
    }
	
	// 장바구니 삭제
	@DeleteMapping
	public ResponseEntity<ResponseDto> deleteCarts(@RequestBody CartDeleteRequest request) {
		String memberId = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
        try {
            if (authentication != null) {
                // 현재 인증된 사용자 정보
                memberId = authentication.getName();
            }

            if (memberId == null)
                return ResponseDto.noAuthentication();
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return ResponseDto.databaseError();
        }

        ResponseEntity<ResponseDto> response = cartService.deleteCarts(memberId, request.getCartIds());
        return response;
    }

}
