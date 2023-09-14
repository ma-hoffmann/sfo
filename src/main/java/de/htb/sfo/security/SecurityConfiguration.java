package de.htb.sfo.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

  private final KeycloakLogoutHandler keycloakLogoutHandler;

  public SecurityConfiguration(final KeycloakLogoutHandler keycloakLogoutHandler) {
    this.keycloakLogoutHandler = keycloakLogoutHandler;
  }

  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
    return authorities -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      var authority = authorities.iterator().next();

      if (authority instanceof OidcUserAuthority oidcUserAuthority) {
        var userInfo = oidcUserAuthority.getUserInfo();

        if (userInfo.hasClaim("realm_access")) {
          var realmAccess = userInfo.getClaimAsMap("realm_access");
          var roles = (Collection<String>) realmAccess.get("roles");
          mappedAuthorities
              .addAll(roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())).toList());
        }
      }
      return mappedAuthorities;
    };
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(
        auth -> auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll()); // <3>

    http.oauth2Login().and().logout().addLogoutHandler(this.keycloakLogoutHandler).logoutSuccessUrl("/");

    super.configure(http);
  }

}