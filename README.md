oauth2-couchbase-token-store
===================================

Couchbase token store for spring security oauth2.

Create Couchbase Spring Oauth2 Token Store in addition of existing InMemory and Jdbc Token Store.


Allow you to manage your tokens via Couchbase server :

How to use it ?
===================================

1) Declare the token store inside your spring oauth2 context configuration

```xml
<!-- Token store -->
<bean id="tokenStore" class="com.tce.spring.oauth2.token.store.SpringDataTokenStore" />
```

2) Bind it to the spring oauth2 token service

```xml
<bean id="tokenServices" class="org.springframework.security.oauth2.provider.token.DefaultTokenServices">
        <property name="tokenStore" ref="tokenStore" />
        <property name="supportRefreshToken" value="true" />
        <property name="clientDetailsService" ref="clientDetails" />
</bean>
```

3) Try it

You can see a full demonstration with my other project right here :

https://github.com/tcompiegne/oauth2-server-spring-couchbase

Informations
===================================

Couchbase tokens are no expiry time (they remain indefinitely inside couchbase)

To set an expiry time you can declare your document like this :

<pre>
@Document(expiry = 1000) 
public class OAuth2AccesToken {
  ...
}
</pre>


Community
===================================

I hope you will enjoy my work, any feedbacks will be grateful.
