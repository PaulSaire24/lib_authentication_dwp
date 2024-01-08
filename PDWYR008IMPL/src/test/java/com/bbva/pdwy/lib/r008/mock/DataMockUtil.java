package com.bbva.pdwy.lib.r008.mock;

import com.bbva.pdwy.dto.auth.salesforce.SalesforceResponseDTO;

public class DataMockUtil {
	private static final DataMockUtil INSTANCE = new DataMockUtil();
	private ObjectMapperHelper objectMapperHelper = ObjectMapperHelper.getInstance();

	public static DataMockUtil getInstance() {
		return INSTANCE;
	}

	private DataMockUtil() {
	}

	public SalesforceResponseDTO getSalesforceResponse() throws java.io.IOException {
		return objectMapperHelper.readValue(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(
						"mock/responseSalesforce.json"),
				SalesforceResponseDTO.class);
	}
}
