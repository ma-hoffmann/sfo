package de.htb.sfo.team.back.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.htb.sfo.team.back.exception.TeamNotFoundException;
import de.htb.sfo.team.back.persistence.entity.Team;
import de.htb.sfo.team.back.persistence.repository.TeamRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeamService implements Serializable {

  private static final long serialVersionUID = -4166227375980627201L;

  private final TeamRepository repository;

  public List<Team> getAllTeams() {
    return this.repository.findAll();
  }

  public void deleteTeam(final Team team) throws TeamNotFoundException {
    if (this.repository.findById(team.getId()).isEmpty()) {
      throw new TeamNotFoundException(team);
    }
    this.repository.delete(team);
  }

  public Optional<Team> getTeamByID(final Long teamID) {
    return this.repository.findById(teamID);
  }

  public Team saveTeam(final Team team) {
    return this.repository.save(team);
  }
}
