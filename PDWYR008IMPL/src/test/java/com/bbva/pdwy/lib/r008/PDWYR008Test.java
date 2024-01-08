package com.bbva.pdwy.lib.r008;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pdwy.dto.auth.salesforce.SalesforceResponseDTO;
import com.bbva.pdwy.lib.r008.impl.PDWYR008Impl;
import com.bbva.pdwy.lib.r008.mock.DataMockUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.aop.framework.Advised;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/PDWYR008-app.xml",
		"classpath:/META-INF/spring/PDWYR008-app-test.xml",
		"classpath:/META-INF/spring/PDWYR008-arc.xml",
		"classpath:/META-INF/spring/PDWYR008-arc-test.xml"})
public class PDWYR008Test {

	@Spy
	private Context context;

	@InjectMocks
	private PDWYR008Impl pdwyR008;

	@Resource(name = "applicationConfigurationService")
	private ApplicationConfigurationService applicationConfigurationService;

	@Mock
	APIConnector externalApiConnector;

	DataMockUtil dataMockUtil = DataMockUtil.getInstance();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		context = new Context();
		ThreadContext.set(context);
		getObjectIntrospection();
	}

	private Object getObjectIntrospection() throws Exception {
		Object result = this.pdwyR008;
		if (this.pdwyR008 instanceof Advised) {
			Advised advised = (Advised) this.pdwyR008;
			result = advised.getTargetSource().getTarget();
		}
		return result;
	}

	@Test
	public void executeGetAuthenticationDataTestOk() throws IOException {
		SalesforceResponseDTO responseDTO = dataMockUtil.getSalesforceResponse();
		ResponseEntity<SalesforceResponseDTO> response = new ResponseEntity<>(responseDTO, HttpStatus.OK);
		Mockito.when(this.applicationConfigurationService.getProperty(Mockito.anyString())).thenReturn("some value");
		Mockito.when(this.externalApiConnector.postForEntity(Mockito.anyString(), Mockito.anyObject(), Mockito.eq(SalesforceResponseDTO.class)))
				.thenReturn(response);
		SalesforceResponseDTO salesforceResponse = pdwyR008.executeGetAuthenticationData("some_id");
		Assert.assertNotNull(salesforceResponse);
		Assert.assertNotNull(salesforceResponse.getAccessToken());
		Assert.assertNotNull(salesforceResponse.getId());
		Assert.assertNotNull(salesforceResponse.getInstanceURL());
		Assert.assertNotNull(salesforceResponse.getIssuedAt());
	}

	@Test(expected = BusinessException.class)
	public void executeGetAuthenticationHttpStatus400Test() {
		Mockito.when(this.applicationConfigurationService.getProperty(Mockito.anyString())).thenReturn("some value");
		Mockito.when(this.externalApiConnector.postForEntity(Mockito.anyString(), Mockito.anyObject(), Mockito.eq(SalesforceResponseDTO.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "bad request", "{\"error\":\"invalid_grant\",\"error_description\":\"authentication failure\"}".getBytes(), StandardCharsets.UTF_8));
		SalesforceResponseDTO SalesforceResponse = pdwyR008.executeGetAuthenticationData("some_id");
		Assert.assertNotNull(SalesforceResponse);
	}

	@Test(expected = BusinessException.class)
	public void executeGetAuthenticationHttpStatus404Test() {
		Mockito.when(this.applicationConfigurationService.getProperty(Mockito.anyString())).thenReturn("some value");
		Mockito.when(this.externalApiConnector.postForEntity(Mockito.anyString(), Mockito.anyObject(), Mockito.eq(SalesforceResponseDTO.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "not found", "</html>\n".getBytes(), StandardCharsets.UTF_8));
		SalesforceResponseDTO SalesforceResponse = pdwyR008.executeGetAuthenticationData("some_id");
		Assert.assertNotNull(SalesforceResponse);
	}

	@Test(expected = BusinessException.class)
	public void executeGetAuthenticationHttpStatus500Test() {
		Mockito.when(this.applicationConfigurationService.getProperty(Mockito.anyString())).thenReturn("some value");
		Mockito.when(this.externalApiConnector.postForEntity(Mockito.anyString(), Mockito.anyObject(), Mockito.eq(SalesforceResponseDTO.class)))
				.thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
		SalesforceResponseDTO SalesforceResponse = pdwyR008.executeGetAuthenticationData("some_id");
		Assert.assertNotNull(SalesforceResponse);
	}

	@Test(expected = BusinessException.class)
	public void executeGetAuthenticationDataTestWithTimeOut() {
		Mockito.when(this.applicationConfigurationService.getProperty(Mockito.anyString())).thenReturn("some value");
		Mockito.when(this.externalApiConnector.postForEntity(Mockito.anyString(), Mockito.anyObject(), Mockito.eq(SalesforceResponseDTO.class)))
				.thenThrow(new TimeoutException("advice_code"));
		pdwyR008.executeGetAuthenticationData("some_id");
	}

}
