package de.htb.sfo.team.front.event;

import com.vaadin.flow.component.ComponentEvent;

import de.htb.sfo.team.back.persistence.entity.Team;
import de.htb.sfo.team.front.TeamEditor;
import lombok.Getter;

public class TeamSaveEvent extends ComponentEvent<TeamEditor> {
    private static final long serialVersionUID = -2165676105920744003L;

    @Getter
    private final Team team;

    public TeamSaveEvent(final TeamEditor source, final Team team) {
        super(source, false);
        this.team = team;
    }

}
