package com.bbva.pdwy.lib.r008.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.pdwy.dto.auth.enums.ApiParameter;
import com.bbva.pdwy.dto.auth.enums.AuthenticationError;
import com.bbva.pdwy.dto.auth.enums.Credential;
import com.bbva.pdwy.dto.auth.salesforce.SalesforceResponseDTO;
import com.bbva.pdwy.lib.r008.impl.util.ApiExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.Collections;

/**
 * The PDWYR008Impl class...
 */
public class PDWYR008Impl extends PDWYR008Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(PDWYR008Impl.class);

	/**
	 * The execute method...
	 */
	@Override
	public SalesforceResponseDTO executeGetAuthenticationData(String apiConnectorKey) {
		LOGGER.info("PDWYR008Impl - executeGetAuthenticationData() START");
		SalesforceResponseDTO responseData = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getBody(apiConnectorKey), httpHeaders);
		try {
			LOGGER.info("executeGetAuthenticationData() - Calling service with request: {}", request);
			ResponseEntity<SalesforceResponseDTO> response = this.externalApiConnector.postForEntity(apiConnectorKey, request, SalesforceResponseDTO.class);
			LOGGER.info("executeGetAuthenticationData() - response: {}", response);
			responseData = response.getBody();
		} catch (RestClientException ex) {
			LOGGER.info("executeGetAuthenticationData() - END with RestClientException: {}", ex.getMessage());
			ApiExceptionHandler handler = new ApiExceptionHandler();
			handler.handler(ex);
		} catch (TimeoutException ex) {
			LOGGER.info("executeGetAuthenticationData() - END with TimeoutException: {}", ex.getMessage());
			throw new BusinessException(AuthenticationError.MAX_TIMEOUT_REACHED.getCode(), false, AuthenticationError.MAX_TIMEOUT_REACHED.getDescription());
		}
		LOGGER.info("PDWYR008Impl - executeGetAuthenticationData() END");
		return responseData;
	}

	private MultiValueMap<String, String> getBody(String apiConnectorKey) {
		LOGGER.info("PDWYR008Impl - getBody() with apiConnectorKey: {}", apiConnectorKey);
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add(Credential.GRANT_TYPE.getValue(), formatParameter(ApiParameter.SALESFORCE_GRANT_TYPE, apiConnectorKey));
		body.add(Credential.CLIENT_ID.getValue(), formatParameter(ApiParameter.SALESFORCE_CLIENT_ID, apiConnectorKey));
		body.add(Credential.CLIENT_SECRET.getValue(), formatParameter(ApiParameter.SALESFORCE_CLIENT_SECRET, apiConnectorKey));
		body.add(Credential.USERNAME.getValue(), formatParameter(ApiParameter.SALESFORCE_USERNAME, apiConnectorKey));
		body.add(Credential.PASSWORD.getValue(), formatParameter(ApiParameter.SALESFORCE_PASSWORD, apiConnectorKey));
		LOGGER.info("PDWYR008Impl - getBody() END");
		return body;
	}

	private String formatParameter(ApiParameter paramKey, String value) {
		return this.applicationConfigurationService.getProperty(String.format(paramKey.getKey(), value));
	}
}
