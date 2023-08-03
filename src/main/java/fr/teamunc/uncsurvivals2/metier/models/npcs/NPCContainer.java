package fr.teamunc.uncsurvivals2.metier.models.npcs;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import fr.teamunc.uncsurvivals2.metier.models.players.UNCPlayer;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class NPCContainer extends UNCEntitiesContainer {
    private final ArrayList<UNCnpc> npcs = new ArrayList<>();
}
