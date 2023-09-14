package de.htb.sfo.front;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@PageTitle("Sportplatzbelegung")
@Route(value = "/home", layout = MainView.class)
@PermitAll
public class HomeView extends VerticalLayout {

  private static final long serialVersionUID = 6956719720887737961L;

  public HomeView() {
    Image logoHTB = new Image("images/test.png", "Logo HTB");
    Image logoSGDHI = new Image("images/logo_sgdhi.png", "Logo HTB");
    add(logoHTB);
    add(logoSGDHI);

    this.setHorizontalComponentAlignment(Alignment.CENTER, logoHTB);
    this.setHorizontalComponentAlignment(Alignment.CENTER, logoSGDHI);
  }

}
