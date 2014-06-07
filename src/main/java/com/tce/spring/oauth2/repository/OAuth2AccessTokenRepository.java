package com.tce.spring.oauth2.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.couchbase.client.protocol.views.Query;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
public interface OAuth2AccessTokenRepository extends CrudRepository<OAuth2AccessToken, String>{

	List<OAuth2AccessToken> findByUserName(Query query);
	
	List<OAuth2AccessToken> findByClientId(Query query);
	
	List<OAuth2AccessToken> findByClientIdAndUserName(Query query);
}
