package com.tce.spring.oauth2.token.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.tce.spring.oauth2.domain.CouchbaseOAuth2AccessToken;
import com.tce.spring.oauth2.domain.CouchbaseOAuth2AuthenticationToAccessToken;
import com.tce.spring.oauth2.domain.CouchbaseOAuth2RefreshToken;
import com.tce.spring.oauth2.service.api.OAuth2AccessTokenService;
import com.tce.spring.oauth2.service.api.OAuth2AuthenticationToAccessTokenService;
import com.tce.spring.oauth2.service.api.OAuth2RefreshTokenService;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
public class SpringDataTokenStore implements TokenStore {
	
	private static final Log LOG = LogFactory.getLog(SpringDataTokenStore.class);

	@Autowired
	private OAuth2AccessTokenService oAuth2AccessTokenService;
	
	@Autowired
	private OAuth2RefreshTokenService oAuth2RefreshTokenService;
	
	@Autowired
	private OAuth2AuthenticationToAccessTokenService oAuth2AuthenticationToAccessTokenService;
	
	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
	
	public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
		this.authenticationKeyGenerator = authenticationKeyGenerator;
	}
	
	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		return readAuthentication(token.getValue());	
	}

	@Override
	public OAuth2Authentication readAuthentication(String token) {
		OAuth2Authentication authentication = null;
		try {
			CouchbaseOAuth2AccessToken accessToken = (CouchbaseOAuth2AccessToken) oAuth2AccessTokenService.findOne(token);
			if (accessToken != null) {
				authentication = deserializeAuthentication(Base64.decodeBase64(accessToken.getAuthentication()));
			} else {
				if (LOG.isInfoEnabled()) {
					LOG.info("Failed to find access token for token " + token);
				}
			}
		} catch (Exception e) {
			LOG.warn("Failed to read authentication for " + token, e);
			removeAccessToken(token);
		}
		return authentication;
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		// Store access token
		CouchbaseOAuth2AccessToken accessToken = new CouchbaseOAuth2AccessToken(token);
		accessToken.setAuthentication(Base64.encodeBase64String(serializeAuthentication(authentication)));
		accessToken.setAuthenticationKey(authenticationKeyGenerator.extractKey(authentication));
		accessToken.setUserName(authentication.isClientOnly() ? null : authentication.getName());
		accessToken.setClientId(authentication.getOAuth2Request().getClientId());
		oAuth2AccessTokenService.save(accessToken);
		
		// Attach access token to authentication
		CouchbaseOAuth2AuthenticationToAccessToken authAccessToken = new CouchbaseOAuth2AuthenticationToAccessToken();
		authAccessToken.setAuthenticationId(accessToken.getAuthenticationKey());
		authAccessToken.setAccessTokenId(token.getValue());
		oAuth2AuthenticationToAccessTokenService.save(authAccessToken);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		OAuth2AccessToken accessToken = null;
		
		try {
			accessToken = oAuth2AccessTokenService.findOne(tokenValue);
			
			if (accessToken == null) {
				if (LOG.isInfoEnabled()) {
					LOG.info("Failed to find access token for token " + tokenValue);
				}
			}
		} catch (Exception e) {
			LOG.warn("Failed to read access token for " + tokenValue, e);
			removeAccessToken(tokenValue);
		}
		
		return accessToken;
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		// remove associate OAuth2AuthenticationToAccessToken Document
		removeAuthenticationToAccessToken(token);
		// then remove access token itself
		removeAccessToken(token.getValue());
	}
	
	private void removeAuthenticationToAccessToken(OAuth2AccessToken token) {
		if (token != null && token instanceof CouchbaseOAuth2AccessToken) {
			CouchbaseOAuth2AccessToken noSQLOAuth2AccessToken = (CouchbaseOAuth2AccessToken) token;
			try {
				oAuth2AuthenticationToAccessTokenService.delete(noSQLOAuth2AccessToken.getAuthenticationKey());
			} catch (Exception e) {
				LOG.error("Could not deletete AuthenticationToAccessToken " + noSQLOAuth2AccessToken, e);
			}
		}
	}
	
	public void removeAccessToken(String value) {
		oAuth2AccessTokenService.delete(value);
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		CouchbaseOAuth2RefreshToken couchBaseRefreshToken = new CouchbaseOAuth2RefreshToken(refreshToken.getValue());
		couchBaseRefreshToken.setAuthentication(Base64.encodeBase64String(serializeAuthentication(authentication)));
		
		OAuth2AccessToken accessToken = getAccessToken(authentication);
		if (accessToken != null) {
			couchBaseRefreshToken.setAccessTokenId(accessToken.getValue());
		} else {
			if (LOG.isInfoEnabled()) {
				LOG.info("Failed to find access token for authentication while storing refresh token " + authentication);
			}
		}
		oAuth2RefreshTokenService.save(couchBaseRefreshToken);
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String token) {
		OAuth2RefreshToken refreshToken = null;
		try {
			refreshToken = oAuth2RefreshTokenService.findOne(token);
			if (refreshToken == null) {
				if (LOG.isInfoEnabled()) {
					LOG.info("Failed to find refresh token for token " + token);
				}
			}
		} catch (Exception e) {
			LOG.warn("Failed to read refresh token for token " + token, e);
			removeRefreshToken(token);
		}
		return refreshToken;
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		return readAuthenticationForRefreshToken(token.getValue());
	}
	
	public OAuth2Authentication readAuthenticationForRefreshToken(String value) {
		OAuth2Authentication authentication = null;
		
		try {
			CouchbaseOAuth2RefreshToken refreshToken = (CouchbaseOAuth2RefreshToken) oAuth2RefreshTokenService.findOne(value);
			if (refreshToken != null) {
				authentication = deserializeAuthentication(Base64.decodeBase64(refreshToken.getAuthentication()));
			} else {
				if (LOG.isInfoEnabled()) {
					LOG.info("Failed to find refresh token for token " + value);
				}
			}
		} catch (Exception e) {
			LOG.warn("Failed to read authentication for refresh token for " + value, e);
			removeRefreshToken(value);
		}
		return authentication;
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		removeRefreshToken(token.getValue());
	}
	
	public void removeRefreshToken(String token) {
		oAuth2RefreshTokenService.delete(token);
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		removeAccessTokenUsingRefreshToken(refreshToken.getValue());
	}
	
	public void removeAccessTokenUsingRefreshToken(String refreshTokenValue) {
		CouchbaseOAuth2RefreshToken refreshToken = (CouchbaseOAuth2RefreshToken) oAuth2RefreshTokenService.findOne(refreshTokenValue);
		if (refreshToken != null) {
			oAuth2AccessTokenService.delete(refreshToken.getAccessTokenId());
		} else {
			LOG.warn("Failed to read refresh token while removing access token using refresh token " + refreshTokenValue);
			removeRefreshToken(refreshTokenValue);
		}
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		OAuth2AccessToken accessToken = null;
		String key = authenticationKeyGenerator.extractKey(authentication);
		CouchbaseOAuth2AuthenticationToAccessToken auth = oAuth2AuthenticationToAccessTokenService.findOne(key);
		
		try {
			if (auth != null) {
				accessToken = oAuth2AccessTokenService.findOne(auth.getAccessTokenId());
			} else {
				if (LOG.isInfoEnabled()) {
					LOG.debug("Failed to find access token for authentication " + authentication);
				}
			}
		} catch (Exception e) {
			LOG.error("Could not get access token for authentication " + authentication, e);
		}
		
		if (accessToken != null
				&& !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
			removeAccessToken(accessToken.getValue());
			// Keep the store consistent (maybe the same user is represented by this authentication but the details have
			// changed)
			storeAccessToken(accessToken, authentication);
		}
		return accessToken;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
		Query query = new Query();
	    query.setKey(ComplexKey.of(clientId, userName));
	    
	    accessTokens = oAuth2AccessTokenService.findByClientIdAndUserName(query);
	    if (accessTokens == null) {
	    	if (LOG.isInfoEnabled()) {
				LOG.info("Failed to find access token for userName " + userName);
			}
	    }
	    
		return accessTokens;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
		Query query = new Query();
	    query.setKey(ComplexKey.of(clientId));
	    
	    accessTokens = oAuth2AccessTokenService.findByClientId(query);
	    if (accessTokens == null) {
	    	if (LOG.isInfoEnabled()) {
				LOG.info("Failed to find access token for clientId " + clientId);
			}
	    }
		return accessTokens;
	}
	
	protected byte[] serializeAccessToken(OAuth2AccessToken token) {
		return SerializationUtils.serialize(token);
	}

	protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
		return SerializationUtils.serialize(token);
	}

	protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
		return SerializationUtils.serialize(authentication);
	}

	protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
		return SerializationUtils.deserialize(token);
	}

	protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
		return SerializationUtils.deserialize(token);
	}

	protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
		return SerializationUtils.deserialize(authentication);
	}
}
