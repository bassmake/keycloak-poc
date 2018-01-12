package sk.bsmk.poc.keycloak.logging;

import org.apache.commons.io.IOUtils;
import org.keycloak.util.BasicAuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Provider
public class LoggingFilter implements ClientResponseFilter {

  private static final Logger log = LoggerFactory.getLogger(LoggerFactory.class);

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

    final String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    final String usernamePassword = usernamePassword(requestContext);

    log.info(
      "\r\nRequest\r\n{}: {}\r\nAuthorization: {}\r\nUsername:Password: {}\r\npayload: {}",
      requestContext.getMethod(),
      requestContext.getUri(),
      authorization,
      usernamePassword,
      requestContext.getEntity()
    );

    final byte[] bytes = IOUtils.toByteArray(responseContext.getEntityStream());
    responseContext.setEntityStream(new ByteArrayInputStream(bytes));

    log.info(
      "\r\nResponse\r\nStatusCode: {}\r\npayload: {}",
      responseContext.getStatus(),
      new String(bytes)
    );
  }

  private static String usernamePassword(ClientRequestContext requestContext) {
    final String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    if (authorization == null) {
      return "none";
    }
    final String[] usernamePassword = BasicAuthHelper.parseHeader(authorization);
    return usernamePassword[0] + ":" + usernamePassword[1];
  }

}
