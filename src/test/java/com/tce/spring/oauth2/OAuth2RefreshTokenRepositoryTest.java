package com.tce.spring.oauth2;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.tce.spring.oauth2.domain.CouchbaseOAuth2RefreshToken;
import com.tce.spring.oauth2.repository.OAuth2RefreshTokenRepository;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/couchbase-token-store-spring-oauth2-context.xml" })
@Ignore
//Note : These tests require a running couchbase server
//Ignore by default (TODO : setting up a embedded couchebase server)
public class OAuth2RefreshTokenRepositoryTest {

	private static final String DEFAULT_TOKEN_ID = "default-token-id";
	private static final String DEFAULT_ACCESS_TOKEN_ID = "default-access-token-id";
	
	@Autowired
	private OAuth2RefreshTokenRepository oauth2RefreshTokenRepository;
	
	@Test
    public void testCreateAndUpdateAndDeleteOauth2AccessTokenRepository() {

		CouchbaseOAuth2RefreshToken defaultToken = createSampleOAuth2RefreshToken();
		CouchbaseOAuth2RefreshToken defaultTokenSaved = oauth2RefreshTokenRepository.save(defaultToken);
        Assert.notNull(defaultTokenSaved);
        Assert.isTrue(DEFAULT_TOKEN_ID.equals(defaultTokenSaved.getValue()));
        
        defaultTokenSaved.setAccessTokenId("new-access-token-id");
        CouchbaseOAuth2RefreshToken defaultTokenUpdated = oauth2RefreshTokenRepository.save(defaultToken);
        Assert.notNull(defaultTokenUpdated);
        // It's an updated object
        // No new document are created
        // The DEFAULT_TOKEN_ID has to remain the same
        Assert.isTrue(DEFAULT_TOKEN_ID.equals(defaultTokenUpdated.getValue()));
        Assert.isTrue("new-access-token-id".equals(defaultTokenUpdated.getAccessTokenId()));
        
        // clean data
        oauth2RefreshTokenRepository.delete(defaultTokenSaved.getValue());
        Assert.isNull(oauth2RefreshTokenRepository.findOne(DEFAULT_TOKEN_ID));
    }
	
	private CouchbaseOAuth2RefreshToken createSampleOAuth2RefreshToken() {
		CouchbaseOAuth2RefreshToken defaultToken = new CouchbaseOAuth2RefreshToken(DEFAULT_TOKEN_ID);
		defaultToken.setAccessTokenId(DEFAULT_ACCESS_TOKEN_ID);
		return defaultToken;
	}
}
