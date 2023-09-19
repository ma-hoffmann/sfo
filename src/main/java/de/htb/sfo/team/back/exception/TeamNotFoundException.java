package de.htb.sfo.team.back.exception;

import de.htb.sfo.team.back.persistence.entity.Team;
import lombok.Getter;

public class TeamNotFoundException extends Exception {

    private static final long serialVersionUID = 1753913728757247516L;

    @Getter
    private final Team team;

    public TeamNotFoundException(final Team team) {
        super();
        this.team = team;
    }
}
