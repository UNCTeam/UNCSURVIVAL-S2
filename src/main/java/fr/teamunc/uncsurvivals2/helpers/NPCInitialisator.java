package fr.teamunc.uncsurvivals2.helpers;

import fr.teamunc.uncsurvivals2.metier.models.npcs.traits.ItemGoalsViewTrait;

public class NPCInitialisator implements IInitialisator {

    @Override
    public void init() {

        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(ItemGoalsViewTrait.class));
    }
}
