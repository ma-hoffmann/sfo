package de.htb.sfo.back;

import java.util.Optional;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.security.AuthenticationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LogoutRestController {

    private final JwtDecoder jwtDecoder;
    private final AuthenticationContext authenticationContext;

    public LogoutRestController(final AuthenticationContext authenticationContext, final ClientRegistrationRepository clientRegistrationRepository){
        this.authenticationContext = authenticationContext;
        var jwkSetUri = clientRegistrationRepository.findByRegistrationId("keycloak").getProviderDetails().getJwkSetUri();
        jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @PostMapping("/back-channel-logout")
    public void logout(@RequestParam("logout_token") String rawLogoutToken) {
        var logoutToken = jwtDecoder.decode(rawLogoutToken);
        var sessionId = logoutToken.getClaimAsString("sid");
        log.info("Got event from keycloak to destroy session [{}]...",sessionId);
        Optional<DefaultOidcUser> user = this.authenticationContext.getAuthenticatedUser(DefaultOidcUser.class);
        if (user.isPresent()) {
            // UI.getCurrent().getPage().setLocation("/logout?success");
        }

    }
}
