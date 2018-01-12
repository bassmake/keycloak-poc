package sk.bsmk.poc.keycloak;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.bsmk.poc.keycloak.services.KeycloakService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KeycloakApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class WithRunningApp {

  @LocalServerPort
  int port;

  @Autowired
  KeycloakService service;

  protected RequestSpecification withToken(String token) {
    return RestAssured
      .given()
      .auth().preemptive().oauth2(token)
      .port(port)
      .when()
      .log().all();
  }

  public String managerToken() {
    return service.directAccessGrantNonConfidentialClient(
      KeycloakServiceTest.REALM,
      "non-confidential-client",
      "manager-user",
      "pass"
    ).getToken();
  }

  public String ownerToken() {
    return service.directAccessGrantNonConfidentialClient(
      KeycloakServiceTest.REALM,
      "non-confidential-client",
      "owner-user",
      "pass"
    ).getToken();
  }
}
