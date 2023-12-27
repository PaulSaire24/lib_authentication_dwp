package com.bbva.pdwy.lib.r008.impl.util;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pdwy.dto.auth.enums.AuthenticationError;
import com.bbva.pdwy.dto.auth.salesforce.ErrorSalesforce;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

public class ApiExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);


	public void handler(RestClientException exception) {
		if (exception instanceof HttpClientErrorException) {
			LOGGER.info("ExceptionHandler - HttpClientErrorException");
			this.clientExceptionHandler((HttpClientErrorException) exception);
		} else {
			LOGGER.info("ExceptionHandler - HttpServerErrorException");
			this.serverExceptionHandler((HttpServerErrorException) exception);
		}
	}

	private void clientExceptionHandler(HttpStatusCodeException clientException) {
		LOGGER.info("HttpStatusCodeException - Response body: {}", clientException.getResponseBodyAsString());
		if (clientException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
			throw new BusinessException(AuthenticationError.WRONG_OR_INVALID_REQUEST.getCode(), false, AuthenticationError.WRONG_OR_INVALID_REQUEST.getDescription());
		}

		if(clientException.getStatusCode().equals(HttpStatus.NOT_FOUND)){
			throw new BusinessException(AuthenticationError.NOT_FOUND_SERVICE.getCode(), false, AuthenticationError.NOT_FOUND_SERVICE.getDescription());
		}
		//TODO: mapping https errors

	}

	private void serverExceptionHandler(HttpServerErrorException serverException) {
		LOGGER.info("HttpStatusCodeException - Response body: {}", serverException.getResponseBodyAsString());
		throw new BusinessException(AuthenticationError.UNAVAILABLE_SERVICE.getCode(), false,
				AuthenticationError.UNAVAILABLE_SERVICE.getDescription());

	}
}
