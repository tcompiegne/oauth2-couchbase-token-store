package com.tce.spring.oauth2.service.api;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
public interface OAuth2RefreshTokenService {

	OAuth2RefreshToken findOne(String token);
	
	OAuth2RefreshToken save(OAuth2RefreshToken refreshToken);
	
	void delete(String token);
}
