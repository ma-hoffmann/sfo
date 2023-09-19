package de.htb.sfo.team.front.event;

import com.vaadin.flow.component.ComponentEvent;

import de.htb.sfo.team.back.persistence.entity.Team;
import de.htb.sfo.team.front.DeleteTeamConfirmationDialog;
import lombok.Getter;

public class TeamDeleteEvent extends ComponentEvent<DeleteTeamConfirmationDialog> {
    private static final long serialVersionUID = -2165676105920744003L;

    @Getter
    private final Team team;

    public TeamDeleteEvent(final DeleteTeamConfirmationDialog source, final Team team) {
        super(source, false);
        this.team = team;
    }

}
