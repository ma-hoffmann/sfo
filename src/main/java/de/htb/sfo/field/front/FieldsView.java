package de.htb.sfo.field.front;

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

import de.htb.sfo.field.back.exception.FieldNotFoundException;
import de.htb.sfo.field.back.persistence.entity.Field;
import de.htb.sfo.field.back.service.FieldService;
import de.htb.sfo.field.front.event.FieldDeleteEvent;
import de.htb.sfo.field.front.event.FieldSaveEvent;
import de.htb.sfo.front.MainView;
import jakarta.annotation.security.PermitAll;

@Route(value = "fields", layout = MainView.class)
@PageTitle("Sportplätze")
@PermitAll
public class FieldsView extends VerticalLayout {

  private static final long serialVersionUID = -8295777595072403053L;

  private final FieldService fieldService;

  private final Grid<Field> grid = new Grid<>(Field.class);
  private final MenuBar menuBar = new MenuBar();

  private SingleSelect<Grid<Field>, Field> fieldSelect;
  private final FieldEditor fieldEditor;

  public FieldsView(final FieldService fieldService) {
    this.fieldService = fieldService;

    this.fieldEditor = new FieldEditor(this::saveField);

    createMenuBar();
    createGrid();
    toggleButtons();

    setMaxWidth("90%");
  }

  private void createMenuBar() {

    this.menuBar.addItem("Neu", e -> {
      this.fieldEditor.setField(new Field());
      this.fieldEditor.setHeaderTitle("Neuer Sportplatz");
      this.fieldEditor.open();
    });

    this.menuBar.addItem("Bearbeiten", e -> {
      this.fieldEditor.setField(this.grid.getSelectionModel().getFirstSelectedItem().get());
      this.fieldEditor.setHeaderTitle("Sportplatz bearbeiten");
      this.fieldEditor.open();
    });

    this.menuBar.addItem("Löschen", e -> {
      new DeleteFieldConfirmationDialog(this.grid.getSelectionModel().getFirstSelectedItem().get(), this::deleteField)
          .open();
    });
    add(this.menuBar);
  }

  private void createGrid() {

    setMargin(true);
    this.grid.setSelectionMode(SelectionMode.SINGLE);
    this.grid.setMaxWidth("95%");
    this.grid.setItems(this.fieldService.getAllFields());
    this.grid.getColumnByKey("id").setVisible(false);
    this.grid.getColumnByKey("version").setVisible(false);

    this.fieldSelect = this.grid.asSingleSelect();
    this.fieldSelect.addValueChangeListener(e -> {
      toggleButtons();
    });
    add(this.grid);

  }

  private void toggleButtons() {
    boolean enabled = this.grid.getSelectionModel().getFirstSelectedItem().isPresent();
    if (enabled) {
      this.fieldEditor.setField(this.grid.getSelectionModel().getFirstSelectedItem().get());
    }
    if (this.menuBar.getItems().size() > 1) {
      this.menuBar.getItems().get(1).setEnabled(enabled);
      this.menuBar.getItems().get(2).setEnabled(enabled);
    }
  }

  private void saveField(final FieldSaveEvent event) {
    try {
      this.fieldService.saveField(event.getField());
      this.grid.setItems(this.fieldService.getAllFields());
    } catch (OptimisticLockingFailureException ex) {
      Notification.show(String.format("Sportplatz %s wurde geändert! Bitte neu laden.", event.getField().getName()))
          .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

  private void deleteField(final FieldDeleteEvent event) {
    try {
      this.fieldService.deleteField(event.getField());
      this.grid.setItems(this.fieldService.getAllFields());
      Notification.show(String.format("Sportplatz %s erfolgreich gelöscht", event.getField().getName()));
    } catch (FieldNotFoundException ex) {
      Notification.show(String.format("Sportplatz %s wurde nicht gefunden!", event.getField().getName()))
          .addThemeVariants(NotificationVariant.LUMO_ERROR);
      this.grid.setItems(this.fieldService.getAllFields());
    }
  }

}