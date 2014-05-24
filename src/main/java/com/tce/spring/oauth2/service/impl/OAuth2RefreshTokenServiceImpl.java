package com.tce.spring.oauth2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import com.tce.spring.oauth2.repository.OAuth2RefreshTokenRepository;
import com.tce.spring.oauth2.service.api.OAuth2RefreshTokenService;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
@Service
public class OAuth2RefreshTokenServiceImpl implements OAuth2RefreshTokenService {

	@Autowired
	private OAuth2RefreshTokenRepository oAuth2RefreshTokenRepository;
	
	@Override
	public OAuth2RefreshToken findOne(String token) {
		return oAuth2RefreshTokenRepository.findOne(token);
	}

	@Override
	public OAuth2RefreshToken save(OAuth2RefreshToken refreshToken) {
		return oAuth2RefreshTokenRepository.save(refreshToken);
	}

	@Override
	public void delete(String token) {
		oAuth2RefreshTokenRepository.delete(token);
	}
}
