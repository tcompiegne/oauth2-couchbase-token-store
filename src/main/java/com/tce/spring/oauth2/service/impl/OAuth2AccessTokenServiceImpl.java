package com.tce.spring.oauth2.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.tce.spring.oauth2.repository.OAuth2AccessTokenRepository;
import com.tce.spring.oauth2.service.api.OAuth2AccessTokenService;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
@Service
public class OAuth2AccessTokenServiceImpl implements OAuth2AccessTokenService {

	@Autowired
	private OAuth2AccessTokenRepository oAuth2AccessTokenRepository;
	
	@Override
	public OAuth2AccessToken findOne(String token) {
		return oAuth2AccessTokenRepository.findOne(token);
	}

	@Override
	public void delete(String token) {
		oAuth2AccessTokenRepository.delete(token);
	}

	@Override
	public OAuth2AccessToken save(OAuth2AccessToken accessToken) {
		return oAuth2AccessTokenRepository.save(accessToken);
	}

	@Override
	// TODO : rewrite this
	public List<OAuth2AccessToken> findByClientIdAndUserName(String clientId, String userName) {
		Query query = new Query();
	    query.setKey(ComplexKey.of(clientId));
		List<OAuth2AccessToken> tokensByClientId = findByClientId(query);
		
		query = new Query();
	    query.setKey(ComplexKey.of(userName));
		List<OAuth2AccessToken> tokensByUserName =  findByUserName(query);
		
		List<OAuth2AccessToken> tokensList = new ArrayList<OAuth2AccessToken>();
		tokensList.addAll(tokensByClientId);
		tokensList.addAll(tokensByUserName);
		
		Map<String, OAuth2AccessToken> tokens = new HashMap<String, OAuth2AccessToken>();
		for (OAuth2AccessToken token : tokensList) {
			tokens.put(token.getValue(), token);
		}
		return new ArrayList<OAuth2AccessToken>(tokens.values());
	}
	
	@Override
	public List<OAuth2AccessToken> findByUserName(Query query) {
		return oAuth2AccessTokenRepository.findByClientId(query);
	}

	@Override
	public List<OAuth2AccessToken> findByClientId(Query query) {
		return oAuth2AccessTokenRepository.findByUserName(query);
	}

}
