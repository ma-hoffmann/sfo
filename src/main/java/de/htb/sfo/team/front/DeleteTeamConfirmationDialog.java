package de.htb.sfo.team.front;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import de.htb.sfo.team.back.persistence.entity.Team;
import de.htb.sfo.team.front.event.TeamDeleteEvent;

@Route("delete-team-confirmation")

public class DeleteTeamConfirmationDialog extends ConfirmDialog implements ComponentEventListener<ConfirmEvent> {

  private static final long serialVersionUID = 3928335669753774013L;

  private final Team team;

  public DeleteTeamConfirmationDialog(final Team team, final ComponentEventListener<TeamDeleteEvent> listener) {

    addListener(TeamDeleteEvent.class, listener);
    this.team = team;
    setHeader("Löschen");
    setText(String.format("Team %s wirklich löschen?", team.getName()));
    setCancelable(true);
    setCancelText("Nein");

    setConfirmText("Ja");
    addConfirmListener(this);
  }

  @Override
  public void onComponentEvent(final ConfirmEvent event) {
    fireEvent(new TeamDeleteEvent(this, this.team));
  }

  public Registration addTeamDeleteListener(final ComponentEventListener<TeamDeleteEvent> listener) {
    return addListener(TeamDeleteEvent.class, listener);
  }
}
