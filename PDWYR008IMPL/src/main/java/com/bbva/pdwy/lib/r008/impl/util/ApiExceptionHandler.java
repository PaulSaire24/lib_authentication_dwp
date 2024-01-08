package com.bbva.pdwy.lib.r008.impl.util;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pdwy.dto.auth.enums.AuthenticationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

public class ApiExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	public BusinessException handler(RestClientException exception) {
		if (exception instanceof HttpClientErrorException) {
			LOGGER.info("ExceptionHandler - HttpClientErrorException");
			return this.clientExceptionHandler((HttpClientErrorException) exception);
		} else {
			LOGGER.info("ExceptionHandler - HttpServerErrorException");
			return this.serverExceptionHandler((HttpServerErrorException) exception);
		}
	}

	private BusinessException clientExceptionHandler(HttpStatusCodeException clientException) {
		LOGGER.info("HttpStatusCodeException - Response body: {}", clientException.getResponseBodyAsString());
		//mapping https errors
		if (clientException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
			return new BusinessException(AuthenticationError.WRONG_OR_INVALID_REQUEST_LOGIN.getCode(), false, AuthenticationError.WRONG_OR_INVALID_REQUEST_LOGIN.getDescription());
		}

		if (clientException.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			return new BusinessException(AuthenticationError.NOT_FOUND_SERVICE.getCode(), false, AuthenticationError.NOT_FOUND_SERVICE.getDescription());
		}

		if (clientException.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
			return new BusinessException(AuthenticationError.UNAUTHORIZED_SERVICE.getCode(), false, AuthenticationError.UNAUTHORIZED_SERVICE.getDescription());
		}

		return new BusinessException(AuthenticationError.ERROR_WHEN_PROCESS_IN_REQUEST.getCode(), false, AuthenticationError.ERROR_WHEN_PROCESS_IN_REQUEST.getDescription());
	}

	private BusinessException serverExceptionHandler(HttpServerErrorException serverException) {
		LOGGER.info("HttpServerErrorException - Response body: {}", serverException.getResponseBodyAsString());
		return new BusinessException(AuthenticationError.UNAVAILABLE_SERVICE.getCode(), false, AuthenticationError.UNAVAILABLE_SERVICE.getDescription());
	}
}
