package de.htb.sfo.field.front;

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

import de.htb.sfo.field.back.persistence.entity.Field;
import de.htb.sfo.field.front.event.FieldSaveEvent;
import jakarta.annotation.security.RolesAllowed;

@Route("add-field-confirmation")
@RolesAllowed("admin")
public class FieldEditor extends Dialog {

    private static final long serialVersionUID = 8833773107242325505L;

    private Field field;

    private final TextField txtFieldName = new TextField("Bezeichnung");

    Binder<Field> binder;

    public FieldEditor(final ComponentEventListener<FieldSaveEvent> listener) {
        addListener(FieldSaveEvent.class, listener);
        this.binder = new Binder<>(Field.class);

        setHeaderTitle("Neuer Sportplatz");

        VerticalLayout dialogLayout = createDialogLayout();
        add(dialogLayout);

        Button cancelButton = new Button("Abbrechen", e -> {
            this.field = null;
            this.txtFieldName.clear();
            close();
        });
        Button saveButton = new Button("Speichern", e -> {
            try {
                this.binder.writeBean(this.field);
                fireEvent(new FieldSaveEvent(this, this.field));

                close();
            } catch (Exception ex) {
                Notification.show(ex.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getFooter().add(cancelButton, saveButton);

        this.binder.forField(this.txtFieldName).bind(Field::getName, Field::setName);

    }

    private VerticalLayout createDialogLayout() {

        VerticalLayout dialogLayout = new VerticalLayout(this.txtFieldName);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    public void setField(final Field field) {
        this.field = field;
        this.binder.readBean(this.field);
        this.txtFieldName.focus();
    }

    public Registration addFieldSaveListener(final ComponentEventListener<FieldSaveEvent> listener) {
        return addListener(FieldSaveEvent.class, listener);
    }
}
