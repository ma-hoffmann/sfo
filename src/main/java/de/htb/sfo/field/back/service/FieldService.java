package de.htb.sfo.field.back.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.htb.sfo.field.back.exception.FieldNotFoundException;
import de.htb.sfo.field.back.persistence.entity.Field;
import de.htb.sfo.field.back.persistence.repository.FieldRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FieldService implements Serializable {

    private static final long serialVersionUID = -4166227375980627201L;

    private final FieldRepository repository;

    public List<Field> getAllFields() {
        return this.repository.findAll();
    }

    public void deleteField(final Field field) throws FieldNotFoundException {
        if (this.repository.findById(field.getId()).isEmpty()) {
            throw new FieldNotFoundException(field);
        }
        this.repository.delete(field);
    }

    public Optional<Field> getFieldByID(final Long fieldID) {
        return this.repository.findById(fieldID);
    }

    public Field saveField(final Field field) {
        return this.repository.save(field);
    }
}
