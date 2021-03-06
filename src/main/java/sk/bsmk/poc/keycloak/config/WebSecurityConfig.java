package sk.bsmk.poc.keycloak.config;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class WebSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    final KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();
    // because spring security need ROLE_ prefix
    provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
    auth.authenticationProvider(provider);
  }

  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    return new NullAuthenticatedSessionStrategy();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http
      .authorizeRequests()
      .antMatchers("/api/owner/**").hasRole("owner")
      .antMatchers("/api/manager/**").hasRole("manager")
      .antMatchers("/authorized-api/**").fullyAuthenticated()
      .anyRequest().permitAll()
      .and()
      .csrf().disable();
  }

  @Bean
  public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
    // to make sure that filters are not registered twice
    KeycloakAuthenticationProcessingFilter filter) {
    final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
    // to make sure that filters are not registered twice
    KeycloakPreAuthActionsFilter filter) {
    final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }

}
