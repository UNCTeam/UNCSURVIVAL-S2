package fr.teamunc.uncsurvivals2;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import fr.teamunc.base_unclib.models.tickloops.UNCPhase;
import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.customblock_unclib.CustomBlockLib;
import fr.teamunc.customblock_unclib.models.UNCCustomBlock;
import fr.teamunc.customblock_unclib.models.UNCCustomBlockType;
import fr.teamunc.customitem_unclib.CustomItemLib;
import fr.teamunc.customitem_unclib.models.*;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomBootsType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomChestplateType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomHelmetType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomLeggingsType;
import fr.teamunc.ekip_unclib.EkipLib;
import fr.teamunc.ekip_unclib.models.UNCTeam;
import fr.teamunc.uncsurvivals2.controllers.NPCController;
import fr.teamunc.uncsurvivals2.controllers.UNCPlayerController;
import fr.teamunc.uncsurvivals2.helpers.BlockInitiator;
import fr.teamunc.uncsurvivals2.helpers.ItemInitiator;
import fr.teamunc.uncsurvivals2.listeners.NPCClickListener;
import fr.teamunc.uncsurvivals2.metier.models.phases.UNCPhase1;
import fr.teamunc.uncsurvivals2.metier.models.phases.UNCPhase2;
import fr.teamunc.uncsurvivals2.metier.models.phases.UNCPhase3;
import fr.teamunc.uncsurvivals2.minecraft.commands_exec.UncSurvivalCommands;
import fr.teamunc.uncsurvivals2.minecraft.eventsListeners.PlayerListener;
import fr.teamunc.zone_unclib.ZoneLib;
import lombok.Getter;
import lombok.val;
import lombok.var;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public final class UNCSurvivalS2 extends JavaPlugin {

    // SINGLETON
    private static UNCSurvivalS2 instance;
    public static UNCSurvivalS2 get() {
        return instance;
    }
    // END SINGLETON

    @Getter
    private UNCPlayerController playerController;

    @Getter
    private NPCController npcController;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        // init plugin folder
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        UNCEntitiesContainer.init(getDataFolder());

        // init team informations
        HashMap<String, Object> teamsinfosModel = new HashMap<>();
        teamsinfosModel.put("money", 0.0);
        teamsinfosModel.put("score", 10.0);

        // init base lib
        EkipLib.init(this, teamsinfosModel);
        BaseLib.init(this);
        CustomItemLib.init(this);
        CustomBlockLib.init(this);
        ZoneLib.init(this,new HashMap<>());

        // init custom items and blocks AFTER recipes
        initCustomObjects();
        initRecipes();
        initScoreboard();
        initController();
        initCommandsAndListeners();

        // init game phases
        initGamePhases();
    }

    private void initCustomObjects() {
        var itemInitiator = new ItemInitiator();
        itemInitiator.init();

        var blockInitiator = new BlockInitiator();
        blockInitiator.init();
    }

    private void initCommandsAndListeners() {
        // Commands
        this.getCommand("test").setExecutor(new UncSurvivalCommands());

        // Listeners
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new NPCClickListener(), this);
    }

    private void initController() {
        this.playerController = new UNCPlayerController();
        this.npcController = new NPCController();
    }

    private void initGamePhases() {
        BaseLib.getUNCPhaseController().registerTickLoop(0, new UNCPhase1());
        BaseLib.getUNCPhaseController().registerTickLoop(1, new UNCPhase2());
        BaseLib.getUNCPhaseController().registerTickLoop(2, new UNCPhase3());
    }

    public void initScoreboard() {
        BaseLib.getUNCScoreboardController().registerUNCScoreboardType(
            "§6§lUNC Survival Saison 2",
                // lines actualiser
                (player) -> {
                    List<String> lines = new ArrayList<>();
                    UNCTeam team = EkipLib.getTeamController().getTeamOfPlayer(player.getUniqueId());
                    UNCPhase phase = BaseLib.getUNCPhaseController().getActualPhaseInstance();

                    // line 0
                    lines.add(ChatColor.GRAY + "=================");

                    if (team != null) {
                        // line 1
                        lines.add(ChatColor.GREEN + "Team: " + ChatColor.GOLD + team.getName());

                        // line 2
                        lines.add(ChatColor.GREEN + "Money: " + ChatColor.GOLD + team.getAdditionalInformation("money", Double.class));

                        // line 3
                        lines.add(ChatColor.GREEN + "Score: " + ChatColor.GOLD + team.getAdditionalInformation("score", Double.class));
                    }
                    // line 4
                    lines.add(ChatColor.GREEN + "Phase: " + ChatColor.GOLD + (phase != null ? phase.getName() : "Aucune"));

                    // line 5
                    lines.add(ChatColor.GREEN + "Fin de phase: ");

                    // line 6
                    Duration durationLeft = BaseLib.getUNCPhaseController().getDurationLeft();
                    lines.add(ChatColor.GREEN + String.format("§b%02d§6j §b%02d§6h §b%02d§6m §b%02d§6s", durationLeft.toDays(), durationLeft.toHours() % 24, durationLeft.toMinutes() % 60, durationLeft.getSeconds() % 60));

                    // line 7
                    lines.add(ChatColor.GRAY + "=================");

                    return lines;
                }
        );
    }

    public void initRecipes() {
        // TODO

        /*NamespacedKey key = new NamespacedKey(this,"craftAmethystSword");
        ItemStack result = CustomItemLib.getUNCCustomItemController().createCustomItem("AMETHYST_SWORD", 1);
        ShapelessRecipe amethystSword = new ShapelessRecipe(key, result)
                .addIngredient(Material.DIAMOND_SWORD)
                .addIngredient(Material.STICK);

        CustomItemLib.getUNCCustomItemController().registerCraft(amethystSword, null, false);

         */
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getPlayerController().save();
        this.getNpcController().save();
    }
}
