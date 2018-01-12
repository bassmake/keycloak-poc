package sk.bsmk.poc.keycloak;

import org.keycloak.adapters.springboot.KeycloakAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = KeycloakAutoConfiguration.class)
public class KeycloakApp {

  public static void main(String ... args) {
    SpringApplication.run(KeycloakApp.class, args);
  }

}
