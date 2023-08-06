package fr.teamunc.uncsurvivals2.metier.models.npcs.traits;

import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import lombok.var;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;

@TraitName("itemgoals")
public class ItemGoalsViewTrait extends Trait {
    private final UNCSurvivalS2 plugin;

    public ItemGoalsViewTrait(String name) {
        super(name);
        plugin = UNCSurvivalS2.get();
    }

    // An example event handler. All traits will be registered automatically as Spigot event Listeners
    @EventHandler
    public void click(net.citizensnpcs.api.event.NPCRightClickEvent event){
        //Handle a click on a NPC. The event has a getNPC() method.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if (!event.getNPC().equals(this.getNPC())) return;

        // show an inventory of itemGoals
        Inventory inv = plugin.getServer().createInventory(null, 27, "ItemGoals");

        // add itemGoals to inventory
        /*for (var keyval : plugin.getItemGoalsController().getItems().entrySet()) {
            //ItemStack
        }*/



        event.getClicker().openInventory(inv);

    }

    // Called every tick
    @Override
    public void run() {
    }

    //Run code when your trait is attached to a NPC.
    //This is called BEFORE onSpawn, so npc.getEntity() will return null
    //This would be a good place to load configurable defaults for new NPCs.
    @Override
    public void onAttach() {
        plugin.getServer().getLogger().info(npc.getName() + "has been assigned MyTrait!");
    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getEntity() is still valid.
    @Override
    public void onDespawn() {
    }

    //Run code when the NPC is spawned. Note that npc.getEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {
    }

}
