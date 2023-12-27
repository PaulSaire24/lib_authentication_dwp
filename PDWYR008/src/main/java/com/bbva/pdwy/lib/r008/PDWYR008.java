package com.bbva.pdwy.lib.r008;

import com.bbva.pdwy.dto.auth.salesforce.SalesforceResponseDTO;

/**
 * The  interface PDWYR008 class...
 */
public interface PDWYR008 {

	/**
	 * The execute method...
	 */
	SalesforceResponseDTO executeGetAuthenticationData(String apiConnectorKey);

}
