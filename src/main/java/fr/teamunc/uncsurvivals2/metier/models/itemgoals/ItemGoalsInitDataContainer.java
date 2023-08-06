package fr.teamunc.uncsurvivals2.metier.models.itemgoals;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import lombok.Getter;

@Getter
public class ItemGoalsInitDataContainer extends UNCEntitiesContainer {

    private ItemGoalData[] items;
}
