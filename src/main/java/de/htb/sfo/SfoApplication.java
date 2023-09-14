package de.htb.sfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@Theme(value = "sfo")
@PWA(shortName = "sfo", name = "sfo", themeColor = "#237523")
@Push(transport = Transport.WEBSOCKET, value = PushMode.AUTOMATIC)
public class SfoApplication implements AppShellConfigurator {

  private static final long serialVersionUID = 7698294620010183418L;

  public static void main(final String[] args) {
    SpringApplication.run(SfoApplication.class, args);
  }

}
