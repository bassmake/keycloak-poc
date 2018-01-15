package sk.bsmk.poc.keycloak;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
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

  @Test
  public void thatManagerCannotAccessOwnerData() {
    final String token = managerToken();

    withToken(token)
      .get("/api/owner/data")
      .then()
      .statusCode(401)
      .log().all();
  }

}
