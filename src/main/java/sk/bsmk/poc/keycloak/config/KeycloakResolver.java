package sk.bsmk.poc.keycloak.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class KeycloakResolver implements KeycloakConfigResolver {

  private static final Logger log = LoggerFactory.getLogger(KeycloakResolver.class);
  private final ApplicationContext context;

  @Autowired
  public KeycloakResolver(ApplicationContext context) {
    this.context = context;
  }

  @Override
  public KeycloakDeployment resolve(HttpFacade.Request facade) {
    try {
      return KeycloakDeploymentBuilder.build(context.getResource("classpath:keycloak/keycloak.json").getInputStream());
    } catch (Exception e) {
      log.error("Keycloak Deployment not built", e);
      throw new RuntimeException(e);
    }
  }

}
