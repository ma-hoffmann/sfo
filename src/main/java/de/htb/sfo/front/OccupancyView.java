package de.htb.sfo.front;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@Route(value = "occupancy", layout = MainView.class)
@PageTitle("Belegungen")
@PermitAll
public class OccupancyView extends VerticalLayout {

  private static final long serialVersionUID = -2277123054732142395L;

  public OccupancyView() {
    Span name = new Span("Seite der Belegungen");
    add(name);
  }
}