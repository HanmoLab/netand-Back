package org.pro.netandback.core.auth.jwt;

import java.io.IOException;

import org.pro.netandback.common.dto.ResponseDto;
import org.pro.netandback.core.error.ErrorCode;
import org.pro.netandback.core.error.exception.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws
		IOException {
		ErrorCode ec = (authException instanceof JwtAuthenticationException jwtEx) ? jwtEx.getErrorCode() :
			ErrorCode.AUTHENTICATION_FAILED;
		ResponseDto<String> body = ResponseDto.of(HttpStatus.valueOf(ec.getStatus()), ec.getMessage(), ec.getCode());
		res.setStatus(ec.getStatus());
		res.setContentType("application/json; charset=UTF-8");
		objectMapper.writeValue(res.getWriter(), body);
	}
}
