package fr.teamunc.uncsurvivals2.metier.models.itemgoals;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import lombok.Getter;
@Getter
public class ItemGoalsInGameContainer extends UNCEntitiesContainer {

    private ItemGoal[] items;
}
