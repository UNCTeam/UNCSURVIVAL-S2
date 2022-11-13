package fr.teamunc.uncsurvivals2.minecraft.eventsListeners;

import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            UNCSurvivalS2.get().getPlayerController().playerMadnessIncreaseRandomly(killer.getUniqueId(), 3);
        }

    }
}
