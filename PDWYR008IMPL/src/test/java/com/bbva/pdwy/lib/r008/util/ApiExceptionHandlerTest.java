package com.bbva.pdwy.lib.r008.util;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pdwy.lib.r008.impl.util.ApiExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.StandardCharsets;

public class ApiExceptionHandlerTest {

	private ApiExceptionHandler apiExceptionHandler;

	@Before
	public void setUp(){
		apiExceptionHandler = new ApiExceptionHandler();
	}

	@Test(expected = BusinessException.class)
	public void handlerHttpClientErrorExceptionTest(){
		String responseBody = "{\"error\":\"invalid_grant\",\"error_description\":\"authentication failure\"}";
		HttpClientErrorException badRequest = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "bad request", responseBody.getBytes(), StandardCharsets.UTF_8);
		apiExceptionHandler.handler(badRequest);

	}

	@Test(expected = BusinessException.class)
	public void handlerHttpServerErrorExceptionTest(){
		String responseBody = "{\"error\":\"invalid_grant\",\"error_description\":\"authentication failure\"}";
		HttpServerErrorException badRequest = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "500", responseBody.getBytes(), StandardCharsets.UTF_8);
		apiExceptionHandler.handler(badRequest);

	}

	@Test(expected = BusinessException.class)
	public void handlerHttpClientErrorException404Test(){
		String responseBody = "<html>...";
		HttpClientErrorException badRequest = new HttpClientErrorException(HttpStatus.NOT_FOUND, "404", responseBody.getBytes(), StandardCharsets.UTF_8);
		apiExceptionHandler.handler(badRequest);

	}
}
