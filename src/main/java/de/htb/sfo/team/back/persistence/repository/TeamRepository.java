package de.htb.sfo.team.back.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.htb.sfo.team.back.persistence.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
