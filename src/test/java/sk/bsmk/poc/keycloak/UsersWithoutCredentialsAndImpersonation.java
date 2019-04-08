package sk.bsmk.poc.keycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Test;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.bsmk.poc.keycloak.logging.LoggingFilter;
import sk.bsmk.poc.keycloak.services.KeycloakService;

import javax.ws.rs.core.Response;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersWithoutCredentialsAndImpersonation {

  private static final String username = "merchant-to-impersonate";

  private static final Logger log = LoggerFactory.getLogger(UsersWithoutCredentialsAndImpersonation.class);

  private final ResteasyClient restEasyClient = new ResteasyClientBuilder()
    .register(LoggingFilter.class)
    .connectionPoolSize(10)
    .build();

  private final Keycloak keycloak = KeycloakBuilder.builder()
    .resteasyClient(restEasyClient)
    .serverUrl(KeycloakService.serverUrl)
    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
    .realm(KeycloakServiceTest.REALM)
    .clientId("confidential-client")
    .clientSecret("8006ff0d-7947-4ae7-8437-bd24c5e5a67a")
    .build();

  @Test
  public void thatUserCanHaveNoCredentials() {

    deleteMerchantUser();

    final UserRepresentation user = new UserRepresentation();
    user.setUsername(username);

    final Response response = realm()
      .users()
      .create(user);

    assertThat(response.getStatus()).isEqualTo(201);

    final Keycloak keycloakImpersonate = KeycloakBuilder.builder()
      .resteasyClient(restEasyClient)
      .serverUrl(KeycloakService.serverUrl)
      .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
      .realm(KeycloakServiceTest.REALM)
      .clientId("confidential-client")
      .clientSecret("8006ff0d-7947-4ae7-8437-bd24c5e5a67a")
      .build();



  }

  private void deleteMerchantUser() {
    final List<UserRepresentation> users = realm().users().search(username);
    if (users.isEmpty()) {
      return;
    }
    if (users.size() > 1) {
      throw new RuntimeException("Multiple users with username {} exist");
    }
    realm().users().delete(users.get(0).getId());
  }

  private RealmResource realm() {
    return keycloak.realm(KeycloakServiceTest.REALM);
  }

}
