package de.htb.sfo.security;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.security.AuthenticationContext;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SecurityService {

  private final AuthenticationContext authenticationContext;

  public SecurityService(final AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;
  }

  public Optional<DefaultOidcUser> getAuthenticatedUser() {
    return this.authenticationContext.getAuthenticatedUser(DefaultOidcUser.class);
  }

  public Set<String> getAuthorities() {
    Collection<? extends GrantedAuthority> authorities = getAuthenticatedUser().get().getAuthorities();
    return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
  }

  @SuppressWarnings("unchecked")
  public boolean hasRole(final String role) {
    boolean result = false;
    if (getAuthenticatedUser().isPresent()) {
      OidcUserInfo userInfo = getAuthenticatedUser().get().getUserInfo();
      if (userInfo.hasClaim("realm_access")) {
        var realmAccess = userInfo.getClaimAsMap("realm_access");
        var roles = (Collection<String>) realmAccess.get("roles");
        result = roles.contains(role);
        log.debug("User: {} hasRole ({})? : {}", userInfo.getFullName(), role, result);
      }
    }
    return result;
  }

  public void logout() {
    this.authenticationContext.logout();
  }

}