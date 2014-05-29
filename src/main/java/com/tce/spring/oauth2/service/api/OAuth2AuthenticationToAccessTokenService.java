package com.tce.spring.oauth2.service.api;

import com.tce.spring.oauth2.domain.CouchbaseOAuth2AuthenticationToAccessToken;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
public interface OAuth2AuthenticationToAccessTokenService {

	CouchbaseOAuth2AuthenticationToAccessToken findOne(String authenticationKey);
	
	CouchbaseOAuth2AuthenticationToAccessToken save(CouchbaseOAuth2AuthenticationToAccessToken authenticationToAccesToken);
	
	void delete(String authenticationKey);
}
