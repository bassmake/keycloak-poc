package sk.bsmk.poc.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import sk.bsmk.poc.keycloak.services.KeycloakService;
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
      "9a9be1e1-90e8-4b8c-83b1-d9230a55c12e"
    );

    log(response);
  }

  @Test
  public void directAccessGrantsConfidentialClient() throws Exception {
    final AccessTokenResponse response =  service.directAccessGrantConfidentialClient(
      REALM,
      "confidential-client",
      "ddd424f3-6136-47c9-a2d0-61f9a96bf2e4",
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
