package fr.teamunc.uncsurvivals2;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PlayerEnderChestContainer extends UNCEntitiesContainer {
    private HashMap<UUID, UUID> customEnderchest = new HashMap<>();
}
