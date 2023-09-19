package de.htb.sfo.front;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

import de.htb.sfo.field.front.FieldsView;
import de.htb.sfo.security.KeycloakLogoutHandler;
import de.htb.sfo.security.SecurityService;
import de.htb.sfo.team.front.TeamsView;

@PageTitle("Sportplatzbelegung")
@AnonymousAllowed
public class MainView extends AppLayout {

    private static final long serialVersionUID = 5493789704541753085L;

    private final String navFieldTitle;

    private final String navTeamsTitle;

    private final String navOccupancyTitle;

    private final String navHomeTitle;

    private final SecurityService securityService;

    private final AuthenticationContext authenticationContext;

    private final KeycloakLogoutHandler logoutHandler;

    public MainView(@Value("${application.navigation.name.fields}") final String fieldsTitle,
            @Value("${application.navigation.name.occupancy}") final String occupancyTitle,
            @Value("${application.navigation.name.teams}") final String teamsTitle,
            @Value("${application.navigation.name.home}") final String homeTitle, final SecurityService securityService,
            final AuthenticationContext authenticationContext, final KeycloakLogoutHandler logoutHandler) {

        this.authenticationContext = authenticationContext;
        this.logoutHandler = logoutHandler;

        this.navTeamsTitle = teamsTitle;
        this.navFieldTitle = fieldsTitle;
        this.navOccupancyTitle = occupancyTitle;
        this.navHomeTitle = homeTitle;
        this.securityService = securityService;
        createHeader();
        if (this.securityService.hasRole("admin") || this.securityService.hasRole("user")) {
            createNavigation();
        }
    }

    private void createHeader() {

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Sportplatzbelegung HTB / SGDHI");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
        addToNavbar(toggle, title);

    }

    private void createNavigation() {
        Tabs tabs = new Tabs();

        tabs.add(createHomeNavigation());

        if (this.securityService.hasRole("admin")) {
            tabs.add(createFieldsNavigation());
            tabs.add(createTeamsNavigation());
        }
        tabs.add(createOccupancyNavigation());
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        addToDrawer(tabs, createFooter());
    }

    private Tab createHomeNavigation() {
        Icon icon = VaadinIcon.HOME.create();
        setIconStyle(icon);

        RouterLink link = new RouterLink(HomeView.class);
        link.add(icon, new Span(this.navHomeTitle));
        link.setTabIndex(-1);

        return new Tab(link);
    }

    private Tab createFieldsNavigation() {
        Icon icon = VaadinIcon.MAP_MARKER.create();
        setIconStyle(icon);

        RouterLink link = new RouterLink(FieldsView.class);
        link.add(icon, new Span(this.navFieldTitle));

        return new Tab(link);
    }

    private Tab createTeamsNavigation() {
        Icon icon = VaadinIcon.GROUP.create();
        setIconStyle(icon);

        RouterLink link = new RouterLink(TeamsView.class);
        link.add(icon, new Span(this.navTeamsTitle));

        return new Tab(link);
    }

    private Tab createOccupancyNavigation() {
        Icon icon = VaadinIcon.SLIDERS.create();
        setIconStyle(icon);

        RouterLink link = new RouterLink(OccupancyView.class);
        link.add(icon, new Span(this.navOccupancyTitle));

        return new Tab(link);
    }

    private void setIconStyle(final Icon icon) {
        icon.getStyle().set("box-sizing", "border-box").set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)").set("padding", "var(--lumo-space-xs)");
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Authentication authentication = getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken token
                && token.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            Avatar avatar = new Avatar(authentication.getName());
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(oidcUser.getFullName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Abmelden", e -> handleLogout());

            layout.add(userMenu);
        }

        return layout;
    }

    public void handleLogout() {
        Optional<DefaultOidcUser> user = this.authenticationContext.getAuthenticatedUser(DefaultOidcUser.class);
        if (user.isPresent()) {
            UI.getCurrent().getPage().setLocation("/logout?success");
        }
    }

}
