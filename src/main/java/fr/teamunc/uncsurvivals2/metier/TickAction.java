package fr.teamunc.uncsurvivals2.metier;

import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import fr.teamunc.uncsurvivals2.helpers.Message;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TickAction extends BukkitRunnable {

    // SINGLETON
    private static TickAction instance;
    private TickAction(){
        super();
    }
    public static TickAction Get() {
        if (instance == null) instance = new TickAction();
        return instance;
    }
    // END SINGLETON

    @Override
    public void run() {
        Bukkit.broadcastMessage("WOW");
    }

    public void StartTickAction() {
        this.runTaskTimer(
                UNCSurvivalS2.Get(),
                0L,
                1L
        );
    }

    public void cancelTickAction() {
        Message.Get().broadcastMessageToConsole("Stoping Tick Loop...");
        this.cancel();
    }
}
