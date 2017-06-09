package com.wirecard.ms.vas.keycloak.controllers;

import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SecuredController {

  private static final Logger log = LoggerFactory.getLogger(SecuredController.class);

  @GetMapping("/api/owner/data")
  public ResponseEntity<SecuredData> getOwnerData(Principal principal) throws Exception {
    logUserInfo(principal);
    log.info("returning data");
    return ResponseEntity.ok(new SecuredData("I should be available for owner role"));
  }


  @GetMapping("/api/manager/data")
  public ResponseEntity<SecuredData> getData(Principal principal) throws Exception {
    logUserInfo(principal);
    log.info("returning data");
    return ResponseEntity.ok(new SecuredData("I should be available for manager role"));
  }

  private static void logUserInfo(Principal principal) {
    if (principal instanceof KeycloakAuthenticationToken) {
      final KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) principal;
      final AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
      log.info("username: {}", token.getPreferredUsername());
      log.info("roles: {}", token.getRealmAccess().getRoles());
    } else {
      log.error("principal is not keycloak type: {}", principal);
    }
  }

}
