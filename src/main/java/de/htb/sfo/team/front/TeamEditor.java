package de.htb.sfo.team.front;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import de.htb.sfo.team.back.persistence.entity.Team;
import de.htb.sfo.team.front.event.TeamSaveEvent;

@Route("add-team-confirmation")
public class TeamEditor extends Dialog {

  private static final long serialVersionUID = 8833773107242325505L;

  private Team team;

  private final TextField txtTeamName = new TextField("Mannschafts-Name");

  Binder<Team> binder;

  public TeamEditor(final ComponentEventListener<TeamSaveEvent> listener) {
    addListener(TeamSaveEvent.class, listener);
    this.binder = new Binder<>(Team.class);

    setHeaderTitle("Neue Mannschaft");

    VerticalLayout dialogLayout = createDialogLayout();
    add(dialogLayout);

    Button cancelButton = new Button("Abbrechen", e -> {
      this.team = null;
      this.txtTeamName.clear();
      close();
    });
    Button saveButton = new Button("Speichern", e -> {
      try {
        this.binder.writeBean(this.team);
        fireEvent(new TeamSaveEvent(this, this.team));

        close();
      } catch (Exception ex) {
        Notification.show(ex.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
      }
    });
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    getFooter().add(cancelButton, saveButton);

    this.binder.forField(this.txtTeamName).bind(Team::getName, Team::setName);
  }

  private VerticalLayout createDialogLayout() {

    VerticalLayout dialogLayout = new VerticalLayout(this.txtTeamName);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

    return dialogLayout;
  }

  public void setTeam(final Team team) {
    this.team = team;
    this.binder.readBean(this.team);
    this.txtTeamName.focus();
  }

  public Registration addTeamSaveListener(final ComponentEventListener<TeamSaveEvent> listener) {
    return addListener(TeamSaveEvent.class, listener);
  }

}
