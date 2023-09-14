package de.htb.sfo.field.front;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import de.htb.sfo.field.back.persistence.entity.Field;
import de.htb.sfo.field.front.event.FieldDeleteEvent;
import jakarta.annotation.security.RolesAllowed;

@Route("delete-field-confirmation")
@RolesAllowed("admin")
public class DeleteFieldConfirmationDialog extends ConfirmDialog implements ComponentEventListener<ConfirmEvent> {

  private static final long serialVersionUID = 3928335669753774013L;

  private final Field field;

  public DeleteFieldConfirmationDialog(final Field field, final ComponentEventListener<FieldDeleteEvent> listener) {

    addListener(FieldDeleteEvent.class, listener);
    this.field = field;
    setHeader("Löschen");
    setText(String.format("Sportplatz %s wirklich löschen?", field.getName()));
    setCancelable(true);
    setCancelText("Nein");

    setConfirmText("Ja");
    addConfirmListener(this);
  }

  @Override
  public void onComponentEvent(final ConfirmEvent event) {
    fireEvent(new FieldDeleteEvent(this, this.field));
  }

  public Registration addFieldDeleteListener(final ComponentEventListener<FieldDeleteEvent> listener) {
    return addListener(FieldDeleteEvent.class, listener);
  }
}
