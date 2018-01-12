package sk.bsmk.poc.keycloak;

import io.restassured.RestAssured;
import org.junit.Test;


public class AuthorizedAccessTest extends WithRunningApp {

  @Test
  public void getAuthorizedIsSecured() {
    RestAssured.given()
      .port(port)
      .when()
      .log().all()
      .get("/authorized-api/resource")
      .then()
      .log().all()
      .statusCode(401);
  }

}
