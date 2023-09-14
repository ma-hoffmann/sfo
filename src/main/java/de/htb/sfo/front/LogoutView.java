package de.htb.sfo.front;

import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.security.AuthenticationContext;

import de.htb.sfo.security.KeycloakLogoutHandler;
import jakarta.annotation.security.PermitAll;

@PageTitle("Sportplatzbelegung")
@Route(value = "logout")
@PermitAll
public class LogoutView extends VerticalLayout {

  private static final long serialVersionUID = -8401282486786972270L;

  private final AuthenticationContext authenticationContext;

  private final KeycloakLogoutHandler logoutHandler;

  public LogoutView(final AuthenticationContext authenticationContext, final KeycloakLogoutHandler logoutHandler) {
    this.authenticationContext = authenticationContext;
    this.logoutHandler = logoutHandler;

    Optional<DefaultOidcUser> user = this.authenticationContext.getAuthenticatedUser(DefaultOidcUser.class);
    if (user.isPresent()) {
      HttpStatusCode httpStatusCode = this.logoutHandler.logoutFromKeycloak(user.get());
      if (httpStatusCode.is2xxSuccessful()) {
        VaadinSession.getCurrent().close();
        UI.getCurrent().getSession().close();
//        WrappedSession httpSession = vSession.getSession();
//        // Invalidate HttpSession
//        httpSession.invalidate();
        UI.getCurrent().getPage().setLocation("/home");
        authenticationContext.logout();
      }
    }
//    Icon icon = VaadinIcon.SIGN_IN.create();
//    Button login = new Button("Login", event -> UI.getCurrent().getPage().setLocation("/"));
//    login.setIcon(icon);
//    add(login);
//
//    this.setHorizontalComponentAlignment(Alignment.CENTER, login);

  }

}
