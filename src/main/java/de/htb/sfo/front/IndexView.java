package de.htb.sfo.front;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Sportplatzbelegung")
@Route(value = "/", layout = MainView.class)
@AnonymousAllowed
public class IndexView extends VerticalLayout {

  private static final long serialVersionUID = 6956719720887737961L;

  public IndexView() {
    Image logoHTB = new Image("images/test.png", "Logo HTB");
    Image logoSGDHI = new Image("images/logo_sgdhi.png", "Logo HTB");
    add(logoHTB);
    add(logoSGDHI);
    this.setHorizontalComponentAlignment(Alignment.CENTER, logoHTB);
    this.setHorizontalComponentAlignment(Alignment.CENTER, logoSGDHI);

    Icon icon = VaadinIcon.SIGN_IN.create();
    Button login = new Button("Login", event -> UI.getCurrent().getPage().setLocation("/home"));
    login.setIcon(icon);
    add(login);

    this.setHorizontalComponentAlignment(Alignment.CENTER, login);

  }

}
