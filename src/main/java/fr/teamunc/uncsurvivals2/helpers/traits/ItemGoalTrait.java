package fr.teamunc.uncsurvivals2.helpers.traits;

import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;

@TraitName("itemgoal")
public class ItemGoalTrait extends Trait {
    private UNCSurvivalS2 plugin;

    public ItemGoalTrait() {
        super("itemgoal");
        plugin = UNCSurvivalS2.get();
    }
}
