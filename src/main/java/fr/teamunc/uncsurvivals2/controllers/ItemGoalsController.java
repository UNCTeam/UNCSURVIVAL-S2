package fr.teamunc.uncsurvivals2.controllers;

import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoal;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoalsInGameContainer;
import lombok.Getter;
import org.bukkit.Material;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ItemGoalsController {
/*
    private final ItemGoalsInGameContainer itemGoalsMapContainer;
    @Getter
    private ConcurrentMap<Material, Integer> items;

    public ItemGoalsController() {
        this.itemGoalsMapContainer = initItemGoalsMapContainer();
        this.items = initItemsInMemory();
    }

    public ItemGoalsInGameContainer initItemGoalsMapContainer() {
        try {
            return UNCEntitiesContainer.loadContainer("itemGoalsMap", ItemGoalsInGameContainer.class);
        } catch (FileNotFoundException e) {
            UNCSurvivalS2.get().getLogger().info("Creating new unc itemGoalsMap container file");
            return new ItemGoalsInGameContainer();
        }
    }

    public List<ItemGoal> initItemsInMemory() {
        return (this.itemGoalsMapContainer.getItems());
    }


    public void save() {
        this.itemGoalsMapContainer.save("itemGoalsMap");
    }*/
}
