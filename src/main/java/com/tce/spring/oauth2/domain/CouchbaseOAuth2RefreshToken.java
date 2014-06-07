package com.tce.spring.oauth2.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
@Document
public class CouchbaseOAuth2RefreshToken extends DefaultOAuth2RefreshToken {
	
	private static final long serialVersionUID = 3446017343005368437L;

	@Id
	private String refreshTokenId;
	
	private String accessTokenId;
	
	private String authentication;
	
	/**
	 * Default constructor for JPA and other serialization tools.
	 */
	@SuppressWarnings("unused")
	private CouchbaseOAuth2RefreshToken() {
		this(null);
	}
	
	public CouchbaseOAuth2RefreshToken(String value) {
		super(value);
		this.refreshTokenId = value;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public String getAccessTokenId() {
		return accessTokenId;
	}

	public void setAccessTokenId(String accessTokenId) {
		this.accessTokenId = accessTokenId;
	}

}
