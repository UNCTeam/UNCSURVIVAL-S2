package fr.teamunc.uncsurvivals2.helpers;

import fr.teamunc.uncsurvivals2.metier.models.npcs.traits.ItemGoalsViewTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class NPCInitiator implements IInitialisator {

    @Override
    public void init() {
        initTrait();
    }

    private void initTrait() {
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ItemGoalsViewTrait.class));
    }
}
