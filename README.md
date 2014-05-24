couchbase-token-store-spring-oauth2
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

2) Bind id to the spring oauth2 token service

```xml
<bean id="tokenServices" class="org.springframework.security.oauth2.provider.token.DefaultTokenServices">
        <property name="tokenStore" ref="tokenStore" />
        <property name="supportRefreshToken" value="true" />
        <property name="clientDetailsService" ref="clientDetails" />
</bean>
```

