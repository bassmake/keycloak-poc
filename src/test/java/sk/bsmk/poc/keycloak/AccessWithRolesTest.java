package sk.bsmk.poc.keycloak;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KeycloakApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessWithRolesTest extends WithRunningApp {

  @Test
  public void thatOwnerCanAccessHisData() {

    final String token = ownerToken();

    withToken(token)
      .get("/api/owner/data")
      .then()
      .statusCode(200)
      .log().all();
  }

  @Test
  public void thatManagerCanAccessHisData() {
    final String token = managerToken();

    withToken(token)
      .get("/api/manager/data")
      .then()
      .statusCode(200)
      .log().all();
  }

}
