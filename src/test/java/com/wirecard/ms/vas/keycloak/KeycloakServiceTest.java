package com.wirecard.ms.vas.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wirecard.ms.vas.keycloak.services.KeycloakService;
import org.junit.Test;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeycloakServiceTest {

  private static final Logger log = LoggerFactory.getLogger(KeycloakServiceTest.class);

  private final ObjectMapper mapper = new ObjectMapper();
  private final ObjectWriter prettyPrinter = mapper.writerWithDefaultPrettyPrinter();
  private final KeycloakService service = new KeycloakService();

  @Test
  public void clientCredentialsGrant() throws Exception {
    final AccessTokenResponse response = service.clientCredentialsGrant(
      "master",
      "client-with-service-account",
      "cba42568-12bb-4b03-83f3-51f0676ce380"
    );

    log(response);
  }

  @Test
  public void directAccessGrantsConfidentialClient() throws Exception {
    final AccessTokenResponse response =  service.directAccessGrantConfidentialClient(
      "master",
      "confidential-client",
      "8040e947-2b9f-462c-b659-ed38f76fa115",
      "test-user",
      "password"
    );

    log(response);
  }

  @Test
  public void directAccessGrantsNonConfidentialClient() throws Exception {
    final AccessTokenResponse response =  service.directAccessGrantNonConfidentialClient(
      "master",
      "non-confidential-client",
      "test-user",
      "password"
    );

    log(response);
  }

  private AccessTokenResponse log(AccessTokenResponse response) throws Exception {
    log.info("Received:\r\n{}", prettyPrinter.writeValueAsString(response));
    try {
      final JWSInput token = new JWSInput(response.getToken());
      final byte[] content = token.getContent();
      log.info("Received token:\r\n{}", prettyPrinter.writeValueAsString(mapper.readTree(content)));
    } catch (JWSInputException e) {
      log.error("Something went wrong.", e);
    }
    return response;
  }

}
