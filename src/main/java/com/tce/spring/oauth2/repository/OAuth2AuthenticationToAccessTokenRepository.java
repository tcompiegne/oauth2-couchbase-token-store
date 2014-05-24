package com.tce.spring.oauth2.repository;

import org.springframework.data.repository.CrudRepository;

import com.tce.spring.oauth2.domain.CouchbaseOAuth2AuthenticationToAccessToken;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
public interface OAuth2AuthenticationToAccessTokenRepository extends CrudRepository<CouchbaseOAuth2AuthenticationToAccessToken, String> {

}
