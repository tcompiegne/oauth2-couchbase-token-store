package com.tce.spring.oauth2.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
@Document
public class CouchbaseOAuth2AuthenticationToAccessToken {
	
	@Id
	private String authenticationId;
	
	private String accessTokenId;
	
	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getAccessTokenId() {
		return accessTokenId;
	}

	public void setAccessTokenId(String accessTokenId) {
		this.accessTokenId = accessTokenId;
	}

}
