package com.bbva.pdwy.lib.r008.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.elara.utility.api.connector.APIConnectorBuilder;
import com.bbva.pdwy.lib.r008.PDWYR008;

/**
 * This class automatically defines the libraries and utilities that it will use.
 */
public abstract class PDWYR008Abstract extends AbstractLibrary implements PDWYR008 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected APIConnector externalApiConnector;

	protected APIConnectorBuilder apiConnectorBuilder;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param externalApiConnector the this.externalApiConnector to set
	*/
	public void setExternalApiConnector(APIConnector externalApiConnector) {
		this.externalApiConnector = externalApiConnector;
	}

	/**
	* @param apiConnectorBuilder the this.apiConnectorBuilder to set
	*/
	public void setApiConnectorBuilder(APIConnectorBuilder apiConnectorBuilder) {
		this.apiConnectorBuilder = apiConnectorBuilder;
	}

}