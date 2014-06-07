package com.tce.spring.oauth2;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.tce.spring.oauth2.domain.CouchbaseOAuth2AccessToken;
import com.tce.spring.oauth2.repository.OAuth2AccessTokenRepository;

/**
 * 
 * @author Titouan COMPIEGNE
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/couchbase-token-store-spring-oauth2-context.xml" })
@Ignore
// Note : These tests require a running couchbase server
// Ignore by default (TODO : setting up a embedded couchebase server)
public class OAuth2AccessTokenRepositoryTest {

	private static final String DEFAULT_TOKEN_ID = "default-token-id";
	private static final String DEFAULT_TOKEN_AUTHENTICATION_KEY = "default-token-authentication-key";
	private static final String DEFAULT_USER_NAME = "default-user-name";
	private static final String DEFAULT_CLIENT_ID = "default-client-id";
	
	@Autowired
	private OAuth2AccessTokenRepository oauth2AccessTokenRepository;
	
	@Test
    public void testCreateAndUpdateAndDeleteOauth2AccessTokenRepository() {

		CouchbaseOAuth2AccessToken defaultToken = createSampleOAuth2AccessToken();
		CouchbaseOAuth2AccessToken defaultTokenSaved = oauth2AccessTokenRepository.save(defaultToken);
        Assert.notNull(defaultTokenSaved);
        Assert.isTrue(DEFAULT_TOKEN_ID.equals(defaultTokenSaved.getValue()));
        
        defaultTokenSaved.setClientId("new-client-id");
        CouchbaseOAuth2AccessToken defaultTokenUpdated = oauth2AccessTokenRepository.save(defaultToken);
        Assert.notNull(defaultTokenUpdated);
        // It's an updated object
        // No new document are created
        // The DEFAULT_TOKEN_ID has to remain the same
        Assert.isTrue(DEFAULT_TOKEN_ID.equals(defaultTokenUpdated.getValue()));
        Assert.isTrue("new-client-id".equals(defaultTokenUpdated.getClientId()));
        
        // clean data
        oauth2AccessTokenRepository.delete(defaultTokenSaved.getValue());
        Assert.isNull(oauth2AccessTokenRepository.findOne(DEFAULT_TOKEN_ID));
    }
	
	// Access data via Couchbase views are asynchronously
	// Random results can happen
	// OK : previous data are stored into the disc, view can retrieve them
	// KO : previous data are still into the memory, view cannot retrieve them
	@Ignore
	@Test
	public void testFindOauth2AccessTokenByUserName() throws InterruptedException {
		CouchbaseOAuth2AccessToken defaultToken = createSampleOAuth2AccessToken();
		oauth2AccessTokenRepository.save(defaultToken);
		
		Query query = new Query();
	    query.setKey(ComplexKey.of(DEFAULT_USER_NAME));
	    List<OAuth2AccessToken> accessTokens = oauth2AccessTokenRepository.findByUserName(query);
	    
	    Assert.notNull(accessTokens);
	    Assert.notEmpty(accessTokens);
	    Assert.isTrue(DEFAULT_TOKEN_ID.equals(accessTokens.get(0).getValue()));
	    
	    // clean data
        oauth2AccessTokenRepository.delete(DEFAULT_TOKEN_ID);
        Assert.isNull(oauth2AccessTokenRepository.findOne(DEFAULT_TOKEN_ID));
	    
	}
	
	// Access data via Couchbase views are asynchronously
	// Random results can happen
	// OK : previous data are stored into the disc, view can retrieve them
	// KO : previous data are still into the memory, view cannot retrieve them
	@Ignore
	@Test
	public void testFindOAuth2AccessTokenByClientId() {
		CouchbaseOAuth2AccessToken defaultToken = createSampleOAuth2AccessToken();
		oauth2AccessTokenRepository.save(defaultToken);
		
		Query query = new Query();
	    query.setKey(ComplexKey.of(DEFAULT_CLIENT_ID));
	    List<OAuth2AccessToken> accessTokens = oauth2AccessTokenRepository.findByClientId(query);
	    
	    Assert.notNull(accessTokens);
	    Assert.notEmpty(accessTokens);
	    Assert.isTrue(DEFAULT_TOKEN_ID.equals(accessTokens.get(0).getValue()));
	    
	    // clean data
        oauth2AccessTokenRepository.delete(DEFAULT_TOKEN_ID);
        Assert.isNull(oauth2AccessTokenRepository.findOne(DEFAULT_TOKEN_ID));
	}
	
	// Access data via Couchbase views are asynchronously
	// Random results can happen
	// OK : previous data are stored into the disc, view can retrieve them
	// KO : previous data are still into the memory, view cannot retrieve them
	@Ignore
	@Test
	public void testFindOAuth2AccessTokenByClientIdAndUserName() {
		CouchbaseOAuth2AccessToken defaultToken = createSampleOAuth2AccessToken();
		oauth2AccessTokenRepository.save(defaultToken);
		
		Query query = new Query();
		query.setKey(ComplexKey.of(DEFAULT_CLIENT_ID, DEFAULT_USER_NAME));
	    List<OAuth2AccessToken> accessTokens = oauth2AccessTokenRepository.findByClientIdAndUserName(query);
	    
	    Assert.notNull(accessTokens);
	    Assert.notEmpty(accessTokens);
	    Assert.isTrue(DEFAULT_TOKEN_ID.equals(accessTokens.get(0).getValue()));
	    
	    // clean data
        oauth2AccessTokenRepository.delete(DEFAULT_TOKEN_ID);
        Assert.isNull(oauth2AccessTokenRepository.findOne(DEFAULT_TOKEN_ID));
	}
	
	
	
	
	private CouchbaseOAuth2AccessToken createSampleOAuth2AccessToken() {
		CouchbaseOAuth2AccessToken defaultToken = new CouchbaseOAuth2AccessToken(DEFAULT_TOKEN_ID);
		defaultToken.setAuthenticationKey(DEFAULT_TOKEN_AUTHENTICATION_KEY);
		defaultToken.setUserName(DEFAULT_USER_NAME);
		defaultToken.setClientId(DEFAULT_CLIENT_ID);
		return defaultToken;
	}
	
}
