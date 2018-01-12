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
  public static final String REALM = "keycloak-poc";

  private final ObjectMapper mapper = new ObjectMapper();
  private final ObjectWriter prettyPrinter = mapper.writerWithDefaultPrettyPrinter();
  private final KeycloakService service = new KeycloakService();

  @Test
  public void clientCredentialsGrant() throws Exception {
    final AccessTokenResponse response = service.clientCredentialsGrant(
      REALM,
      "client-with-service-account",
      "d88c2a02-1275-4cdf-8ef9-2679c271c5db"
    );

    log(response);
  }

  @Test
  public void directAccessGrantsConfidentialClient() throws Exception {
    final AccessTokenResponse response =  service.directAccessGrantConfidentialClient(
      REALM,
      "confidential-client",
      "52dafc8a-a034-49fc-8cf7-b8bacc40fabd",
      "test-user",
      "password"
    );

    log(response);
  }

  @Test
  public void directAccessGrantsNonConfidentialClient() throws Exception {
    final AccessTokenResponse response =  service.directAccessGrantNonConfidentialClient(
      REALM,
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
