package com.bbva.pdwy.lib.r008.util;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pdwy.dto.auth.enums.AuthenticationError;
import com.bbva.pdwy.lib.r008.impl.util.ApiExceptionHandler;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.StandardCharsets;

public class ApiExceptionHandlerTest {

	private final ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();


	@Test
	public void handlerHttpClientErrorExceptionTest() {
		String responseBody = "{\"error\":\"invalid_grant\",\"error_description\":\"authentication failure\"}";
		HttpClientErrorException badRequest = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "bad request", responseBody.getBytes(), StandardCharsets.UTF_8);
		BusinessException error = apiExceptionHandler.handler(badRequest);
		Assert.assertEquals(AuthenticationError.WRONG_OR_INVALID_REQUEST_LOGIN.getCode(), error.getAdviceCode());
	}

	@Test
	public void handlerHttpServerErrorExceptionTest() {
		String responseBody = "{\"error\":\"invalid_grant\",\"error_description\":\"authentication failure\"}";
		HttpServerErrorException badRequest = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "500", responseBody.getBytes(), StandardCharsets.UTF_8);
		BusinessException error = apiExceptionHandler.handler(badRequest);
		Assert.assertEquals(AuthenticationError.UNAVAILABLE_SERVICE.getCode(), error.getAdviceCode());

	}

	@Test
	public void handlerHttpClientErrorException404Test() {
		String responseBody = "<html>...";
		HttpClientErrorException badRequest = new HttpClientErrorException(HttpStatus.NOT_FOUND, "404", responseBody.getBytes(), StandardCharsets.UTF_8);
		BusinessException error = apiExceptionHandler.handler(badRequest);
		Assert.assertEquals(AuthenticationError.NOT_FOUND_SERVICE.getCode(), error.getAdviceCode());
	}

	@Test
	public void handlerHttpClientErrorException401Test() {
		String responseBody = "[{\"message\":\"Session expired or invalid\",\"errorCode\":\"INVALID_SESSION_ID\"}]";
		HttpClientErrorException badRequest = new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "401", responseBody.getBytes(), StandardCharsets.UTF_8);
		BusinessException error = apiExceptionHandler.handler(badRequest);
		Assert.assertEquals(AuthenticationError.UNAUTHORIZED_SERVICE.getCode(), error.getAdviceCode());
	}

	@Test
	public void handlerHttpClientErrorException403Test() {
		String responseBody = "{\"error\":\"invalid method http\",\"error_description\":\"authentication failure\"}";
		HttpClientErrorException badRequest = new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED, "not allowed", responseBody.getBytes(), StandardCharsets.UTF_8);
		BusinessException error = apiExceptionHandler.handler(badRequest);
		Assert.assertEquals(AuthenticationError.ERROR_WHEN_PROCESS_IN_REQUEST.getCode(), error.getAdviceCode());
	}
}
