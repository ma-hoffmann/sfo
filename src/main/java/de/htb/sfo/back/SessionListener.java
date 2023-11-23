package de.htb.sfo.back;

import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.vaadin.flow.spring.security.AuthenticationContext;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@AllArgsConstructor
public class SessionListener implements HttpSessionListener{

    private final AuthenticationContext authenticationContext;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.info("Got session destroyed event...");
        HttpSessionListener.super.sessionDestroyed(se);
        Optional<DefaultOidcUser> user = this.authenticationContext.getAuthenticatedUser(DefaultOidcUser.class);
        if (user.isPresent()) {
            // UI.getCurrent().getPage().setLocation("/logout?success");
        }
    }

}
