package fr.teamunc.uncsurvivals2.controllers;


import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import fr.teamunc.uncsurvivals2.metier.models.npcs.NPCContainer;
import fr.teamunc.uncsurvivals2.metier.models.npcs.UNCnpc;
import fr.teamunc.uncsurvivals2.metier.models.npcs.traits.ItemGoalsViewTrait;

import java.io.FileNotFoundException;
import java.util.List;

public class NPCController {
    private final NPCContainer npcContainer;

    public NPCController() {
        this.npcContainer = initNPCContainer();
        Message.Get().broadcastMessageToConsole("[NPCController] : Loading " + getNpcs().size() + " unc npc data");
    }

    public void initNPCTraits() {
        //Register your trait with Citizens.
        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(ItemGoalsViewTrait.class));
    }

    public void resetNPC() {
        this.npcContainer.getNpcs().clear();
    }

    public NPCContainer initNPCContainer() {
        try {
            return UNCEntitiesContainer.loadContainer("npcs", NPCContainer.class);
        } catch (FileNotFoundException e) {
            UNCSurvivalS2.get().getLogger().info("Creating new unc npcs container file");
            return new NPCContainer();
        }
    }

    public List<UNCnpc> getNpcs() {
        return this.npcContainer.getNpcs();
    }

    public void addNpcs(UNCnpc npc) {
        this.getNpcs().add(npc);
    }

    public void removeNpcs(UNCnpc npc) {
        this.getNpcs().remove(npc);
    }

    public void save() {
        this.npcContainer.save("npcs");
    }
}
