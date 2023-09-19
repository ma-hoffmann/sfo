package de.htb.sfo.field.back.exception;

import de.htb.sfo.field.back.persistence.entity.Field;
import lombok.Getter;

public class FieldNotFoundException extends Exception {

    private static final long serialVersionUID = 1753913728757247516L;

    @Getter
    private final Field field;

    public FieldNotFoundException(final Field field) {
        super();
        this.field = field;
    }
}
