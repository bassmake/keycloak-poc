package com.wirecard.ms.vas.keycloak.services;

import com.wirecard.ms.vas.keycloak.logging.LoggingFilter;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
public class KeycloakService {

  public static final String serverUrl = "http://localhost:8080/auth";

  private final ResteasyClient restEasyClient = new ResteasyClientBuilder()
    .register(LoggingFilter.class)
    .connectionPoolSize(10)
    .build();

  public AccessTokenResponse byAuthorizationCode(
    String realm,
    String clientId,
    String clientSecret,
    String authorizationCode
  ) {
    final Keycloak keycloak = KeycloakBuilder.builder()
      .resteasyClient(restEasyClient)
      .serverUrl(serverUrl)
      .realm(realm)
      .clientId(clientId)
      .clientSecret(clientSecret)
      .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
      .authorization(authorizationCode)
      .build();

    return keycloak.tokenManager().getAccessToken();
  }

  public AccessTokenResponse clientCredentialsGrant(
    String realm,
    String clientId,
    String clientSecret
  ) {
    final Keycloak keycloak = KeycloakBuilder.builder()
      .resteasyClient(restEasyClient)
      .serverUrl(serverUrl)
      .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
      .realm(realm)
      .clientId(clientId)
      .clientSecret(clientSecret)
      .build();

    return keycloak.tokenManager().getAccessToken();
  }


  public AccessTokenResponse directAccessGrantNonConfidentialClient(
    String realm,
    String clientId,
    String username,
    String password
  ) {
    final Keycloak keycloak = KeycloakBuilder.builder()
      .resteasyClient(restEasyClient)
      .serverUrl(serverUrl)
      .grantType(OAuth2Constants.PASSWORD)
      .realm(realm)
      .clientId(clientId)
      .username(username)
      .password(password)
      .build();

    return keycloak.tokenManager().getAccessToken();
  }

  public AccessTokenResponse directAccessGrantConfidentialClient(
    String realm,
    String clientId,
    String clientSecret,
    String username,
    String password
  ) {
    final Keycloak keycloak = KeycloakBuilder.builder()
      .resteasyClient(restEasyClient)
      .serverUrl(serverUrl)
      .grantType(OAuth2Constants.PASSWORD)
      .realm(realm)
      .clientId(clientId)
      .clientSecret(clientSecret)
      .username(username)
      .password(password)
      .build();

    return keycloak.tokenManager().getAccessToken();
  }

}
