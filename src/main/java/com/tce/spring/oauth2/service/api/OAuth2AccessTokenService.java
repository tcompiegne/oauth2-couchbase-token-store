package com.tce.spring.oauth2.service.api;

import java.util.List;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.couchbase.client.protocol.views.Query;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
public interface OAuth2AccessTokenService {

	OAuth2AccessToken findOne(String token);
	
	void delete(String token);
	
	OAuth2AccessToken save(OAuth2AccessToken accessToken);
	
	List<OAuth2AccessToken> findByClientIdAndUserName(Query query);
	
	List<OAuth2AccessToken> findByClientId(Query query);
	
	List<OAuth2AccessToken> findByUserName(Query query);
}
