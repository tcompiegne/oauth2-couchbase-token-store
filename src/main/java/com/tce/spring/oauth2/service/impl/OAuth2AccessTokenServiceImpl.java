package com.tce.spring.oauth2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

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
	public List<OAuth2AccessToken> findByClientIdAndUserName(Query query) {
		return oAuth2AccessTokenRepository.findByClientIdAndUserName(query);
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
