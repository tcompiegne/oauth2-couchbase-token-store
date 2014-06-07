package com.tce.spring.oauth2.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
@Document
public class CouchbaseOAuth2AccessToken extends DefaultOAuth2AccessToken {

	private static final long serialVersionUID = 6537949925775752989L;

	@Id
	private String tokenId;

	private String authentication;
	
	private String authenticationKey;
	
	private String clientId;
	
	private String userName;
	
	/**
	 * Private constructor for JPA and other serialization tools.
	 */
	@SuppressWarnings("unused")
	private CouchbaseOAuth2AccessToken() {
		this((String) null);
	}

	public CouchbaseOAuth2AccessToken(OAuth2AccessToken accessToken) {
		super(accessToken);
		this.tokenId = accessToken.getValue();
	}

	public CouchbaseOAuth2AccessToken(String value) {
		super(value);
		this.tokenId = value;
	}
	
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public String getAuthenticationKey() {
		return authenticationKey;
	}

	public void setAuthenticationKey(String authenticationKey) {
		this.authenticationKey = authenticationKey;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
