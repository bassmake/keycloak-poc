package sk.bsmk.poc.keycloak;

import io.restassured.RestAssured;
import org.junit.Test;


public class AuthorizedAccessTest extends WithRunningApp {

  @Test
  public void getResourceIsNotAvailableWithoutToken() {
    RestAssured.given()
      .port(port)
      .when()
      .log().all()
      .get("/authorized-api/resource")
      .then()
      .log().all()
      .statusCode(400);
  }

  @Test
  public void thatOwnerResourceIsNotAvailableForManager() {
    final String token = managerToken();

    withToken(token)
      .get("/authorized-api/owner-resource")
      .then()
      .log().all()
      .statusCode(403);
  }

  @Test
  public void getResourceCanByAccessedByManager() {
    final String token = managerToken();

    withToken(token)
      .get("/authorized-api/resource")
      .then()
      .log().all()
      .statusCode(200);
  }

  @Test
  public void postResourceCannotBeAccessedByManager() {
    final String token = managerToken();

    withToken(token)
      .post("/authorized-api/resource")
      .then()
      .log().all()
      .statusCode(403);
  }

  @Test
  public void getResourceCanBeAccessedByOwner() {
    final String token = ownerToken();

    withToken(token)
      .get("/authorized-api/resource")
      .then()
      .log().all()
      .statusCode(200);
  }

  @Test
  public void postResourceCanBeAccessedByOwner() {
    final String token = ownerToken();

    withToken(token)
      .post("/authorized-api/resource")
      .then()
      .log().all()
      .statusCode(200);
  }

}
