package de.htb.sfo.team.front;

import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.htb.sfo.front.MainView;
import de.htb.sfo.team.back.exception.TeamNotFoundException;
import de.htb.sfo.team.back.persistence.entity.Team;
import de.htb.sfo.team.back.service.TeamService;
import de.htb.sfo.team.front.event.TeamDeleteEvent;
import de.htb.sfo.team.front.event.TeamSaveEvent;
import jakarta.annotation.security.PermitAll;

@Route(value = "teams", layout = MainView.class)
@PageTitle("Mannschaften")
@PermitAll
public class TeamsView extends VerticalLayout {

  private static final long serialVersionUID = -8295777595072403053L;

  private final TeamService teamService;

  private final Grid<Team> grid = new Grid<>(Team.class);
  private final MenuBar menuBar = new MenuBar();

  private SingleSelect<Grid<Team>, Team> teamSelect;
  private final TeamEditor teamEditor;

  public TeamsView(final TeamService teamService) {
    this.teamService = teamService;

    this.teamEditor = new TeamEditor(this::saveTeam);

    createMenuBar();
    createGrid();
    toggleButtons();

    setMaxWidth("90%");
  }

  private void createMenuBar() {

    this.menuBar.addItem("Neu", e -> {
      this.teamEditor.setTeam(new Team());
      this.teamEditor.setHeaderTitle("Neue Mannschaft");
      this.teamEditor.open();
    });

    this.menuBar.addItem("Bearbeiten", e -> {
      this.teamEditor.setTeam(this.grid.getSelectionModel().getFirstSelectedItem().get());
      this.teamEditor.setHeaderTitle("Mannschaft bearbeiten");
      this.teamEditor.open();
    });

    this.menuBar.addItem("Löschen", e -> {
      new DeleteTeamConfirmationDialog(this.grid.getSelectionModel().getFirstSelectedItem().get(), this::deleteTeam)
          .open();
    });
    add(this.menuBar);
  }

  private void createGrid() {

    setMargin(true);
    this.grid.setSelectionMode(SelectionMode.SINGLE);
    this.grid.setMaxWidth("95%");
    this.grid.setItems(this.teamService.getAllTeams());
    this.grid.getColumnByKey("id").setVisible(false);
    this.grid.getColumnByKey("version").setVisible(false);

    this.teamSelect = this.grid.asSingleSelect();
    this.teamSelect.addValueChangeListener(e -> {
      toggleButtons();
    });
    add(this.grid);

  }

  private void toggleButtons() {
    boolean enabled = this.grid.getSelectionModel().getFirstSelectedItem().isPresent();
    if (enabled) {
      this.teamEditor.setTeam(this.grid.getSelectionModel().getFirstSelectedItem().get());
    }
    if (this.menuBar.getItems().size() > 1) {
      this.menuBar.getItems().get(1).setEnabled(enabled);
      this.menuBar.getItems().get(2).setEnabled(enabled);
    }
  }

  private void saveTeam(final TeamSaveEvent event) {
    try {
      this.teamService.saveTeam(event.getTeam());
      this.grid.setItems(this.teamService.getAllTeams());
    } catch (OptimisticLockingFailureException ex) {
      Notification.show(String.format("Team %s wurde geändert! Bitte neu laden.", event.getTeam().getName()))
          .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

  private void deleteTeam(final TeamDeleteEvent event) {
    try {
      this.teamService.deleteTeam(event.getTeam());
      this.grid.setItems(this.teamService.getAllTeams());
      Notification.show(String.format("Team %s erfolgreich gelöscht", event.getTeam().getName()));
    } catch (TeamNotFoundException ex) {
      Notification.show(String.format("Team %s wurde nicht gefunden!", event.getTeam().getName()))
          .addThemeVariants(NotificationVariant.LUMO_ERROR);
      this.grid.setItems(this.teamService.getAllTeams());
    }
  }

}
