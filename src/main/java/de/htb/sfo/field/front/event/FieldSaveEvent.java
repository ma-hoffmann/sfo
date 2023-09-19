package de.htb.sfo.field.front.event;

import com.vaadin.flow.component.ComponentEvent;

import de.htb.sfo.field.back.persistence.entity.Field;
import de.htb.sfo.field.front.FieldEditor;
import lombok.Getter;

public class FieldSaveEvent extends ComponentEvent<FieldEditor> {
    private static final long serialVersionUID = -2165676105920744003L;

    @Getter
    private final Field field;

    public FieldSaveEvent(final FieldEditor source, final Field field) {
        super(source, false);
        this.field = field;
    }

}
