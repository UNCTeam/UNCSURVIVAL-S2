package fr.teamunc.uncsurvivals2;

import fr.teamunc.uncsurvivals2.helpers.Message;
import fr.teamunc.uncsurvivals2.metier.TickAction;
import fr.teamunc.uncsurvivals2.minecraft.Initialisator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class UNCSurvivalS2 extends JavaPlugin {

    // SINGLETON
    private static UNCSurvivalS2 instance;
    public static UNCSurvivalS2 Get() {
        return instance;
    }
    // END SINGLETON

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        TickAction.Get().StartTickAction();

        // Init Cmd Exec and Events Listeners
        Initialisator.Get().initCmdExecAndEvntListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().runTask(this,() -> TickAction.Get().cancelTickAction());


        Message.Get().broadcastMessageToConsole("Stoping plugin");
    }
}
