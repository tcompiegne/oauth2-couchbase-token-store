package com.tce.spring.oauth2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
public interface OAuth2RefreshTokenRepository extends CrudRepository<OAuth2RefreshToken, String> {

}
