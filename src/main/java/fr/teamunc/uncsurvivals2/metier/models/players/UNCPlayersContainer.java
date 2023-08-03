package fr.teamunc.uncsurvivals2.metier.models.players;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class UNCPlayersContainer extends UNCEntitiesContainer {
    private final ArrayList<UNCPlayer> players = new ArrayList<>();
}
