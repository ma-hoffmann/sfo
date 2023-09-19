package de.htb.sfo.security;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KeycloakLogoutHandler implements LogoutHandler {

    private final RestTemplate restTemplate;

    public KeycloakLogoutHandler() {
        this.restTemplate = new RestTemplateBuilder().build();
    }

    @Override
    public void logout(final HttpServletRequest request, final HttpServletResponse response,
            final Authentication auth) {
        logoutFromKeycloak((OidcUser) auth.getPrincipal());

    }

    public HttpStatusCode logoutFromKeycloak(final OidcUser user) {
        String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endSessionEndpoint)
                .queryParam("id_token_hint", user.getIdToken().getTokenValue());

        ResponseEntity<String> logoutResponse = this.restTemplate.getForEntity(builder.toUriString(), String.class);
        if (logoutResponse.getStatusCode().is2xxSuccessful()) {
            log.info("Successfully logged out [{}] from Keycloak", user.getName());

        } else {
            log.error("Could not propagate logout to Keycloak");
        }
        return logoutResponse.getStatusCode();
    }

}