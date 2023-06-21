# TODOs

### [Click here for official auth0 doc](https://auth0.com/docs/quickstart/backend/java-spring-security5/01-authorization)

## install dependencies 

## application.yaml

```yaml
auth0:
  audience: https://auth0-aclue-api.com
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://kristoffer-schaaf-aclue.us.auth0.com/
```

## AudienceValidator.java

```java
package aclue.auth0_session_backend.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String audience;

    AudienceValidator(String audience) {
        this.audience = audience;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        OAuth2Error error = new OAuth2Error("invalid_token", "The required audience is missing", null);

        if (jwt.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
}
```

## SecurityConfig.java

```java
package aclue.auth0_session_backend.security;

import aclue.auth0_session_backend.controller.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestOperations;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@EnableWebSecurity
public class SecurityConfig {

    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, Api.CUSTOMERS_PATH).hasAuthority(Api.PERMISSION_READ_CUSTOMERS)
                .mvcMatchers(HttpMethod.POST, Api.CUSTOMERS_PATH).hasAuthority(Api.PERMISSION_WRITE_CUSTOMERS)
                .anyRequest().authenticated()
                .and().cors()
                .and().oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverterWithCustomClaimName("permissions"))
                .decoder(jwtDecoder);
        return http.build();
    }

    private static JwtAuthenticationConverter jwtAuthenticationConverterWithCustomClaimName(String customClaimName) {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(customClaimName);
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }

    @Bean
    RestOperations restOperations(RestTemplateBuilder builder) {
        RestOperations rest = builder
                .setConnectTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .build();
        return rest;
    }
}
```
