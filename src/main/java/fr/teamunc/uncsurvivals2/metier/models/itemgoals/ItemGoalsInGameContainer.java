package fr.teamunc.uncsurvivals2.metier.models.itemgoals;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemGoalsInGameContainer extends UNCEntitiesContainer {

    private List<ItemGoal> items = new ArrayList<>();
}
