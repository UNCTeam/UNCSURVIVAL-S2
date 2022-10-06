package fr.teamunc.uncsurvivals2;

import fr.teamunc.uncsurvivals2.minecraft.commandsExec.UncSurvivalCommands;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class UNCSurvivalS2 extends JavaPlugin {

    // SINGLETON
    private static UNCSurvivalS2 instance;
    public static UNCSurvivalS2 Get() {
        return instance;
    }
    // END SINGLETON

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        // init plugin folder
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        // init base lib

        // init test lib
        this.getCommand("test").setExecutor(new UncSurvivalCommands());
    }


    @Override
    public void onDisable() {

    }
}
