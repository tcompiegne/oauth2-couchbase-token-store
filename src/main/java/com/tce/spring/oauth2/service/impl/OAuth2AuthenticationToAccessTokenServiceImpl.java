package com.tce.spring.oauth2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tce.spring.oauth2.domain.CouchbaseOAuth2AuthenticationToAccessToken;
import com.tce.spring.oauth2.repository.OAuth2AuthenticationToAccessTokenRepository;
import com.tce.spring.oauth2.service.api.OAuth2AuthenticationToAccessTokenService;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
@Service
public class OAuth2AuthenticationToAccessTokenServiceImpl implements OAuth2AuthenticationToAccessTokenService {

	@Autowired
	private OAuth2AuthenticationToAccessTokenRepository oAuth2AuthenticationToAccessTokenRepository;
	
	@Override
	public CouchbaseOAuth2AuthenticationToAccessToken findOne(String authenticationKey) {
		return oAuth2AuthenticationToAccessTokenRepository.findOne(authenticationKey);
	}

	@Override
	public CouchbaseOAuth2AuthenticationToAccessToken save(CouchbaseOAuth2AuthenticationToAccessToken authenticationToAccesToken) {
		return oAuth2AuthenticationToAccessTokenRepository.save(authenticationToAccesToken);
	}

	@Override
	public void delete(String authenticationKey) {
		oAuth2AuthenticationToAccessTokenRepository.delete(authenticationKey);
	}
}
