package de.htb.sfo.field.back.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.htb.sfo.field.back.persistence.entity.Field;

public interface FieldRepository extends JpaRepository<Field, Long> {

}
