package sk.bsmk.poc.keycloak.controllers;

public class SecuredData {

  private final String data;

  public SecuredData(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }

}
