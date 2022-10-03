package fr.teamunc.uncsurvivals2;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.ekip_unclib.EkipLib;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase1;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase2;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase3;
import fr.teamunc.uncsurvivals2.minecraft.commandsExec.UncSurvivalCommands;
import org.bukkit.Bukkit;
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

        // init team informations
        HashMap<String, Object> teamsinfosModel = new HashMap<>();
        teamsinfosModel.put("money", 0);

        // init base lib
        EkipLib.init(this, teamsinfosModel);
        BaseLib.init(this);

        this.getCommand("test").setExecutor(new UncSurvivalCommands());

        BaseLib.getUNCPhaseController().registerTickLoop(0, new UNCPhase1());
        BaseLib.getUNCPhaseController().registerTickLoop(1, new UNCPhase2());
        BaseLib.getUNCPhaseController().registerTickLoop(2, new UNCPhase3());
        BaseLib.getUNCPhaseController().registerFinalExpression(() -> {
            Bukkit.broadcastMessage("FIN DU JEU");
        });
    }

    @Override
    public void onDisable() {

    }
}
