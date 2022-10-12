package fr.teamunc.uncsurvivals2;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.customitem_unclib.CustomItemLib;
import fr.teamunc.customitem_unclib.controllers.UNCCustomItemController;
import fr.teamunc.customitem_unclib.models.UNCCustomPickaxeType;
import fr.teamunc.customitem_unclib.models.UNCCustomSwordType;
import fr.teamunc.ekip_unclib.EkipLib;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase1;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase2;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase3;
import fr.teamunc.uncsurvivals2.minecraft.commandsExec.UncSurvivalCommands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        CustomItemLib.init(this);

        this.getCommand("test").setExecutor(new UncSurvivalCommands());

        // init custom items and AFTER recipes
        initCustomItems();
        initRecipes();

        // init game phases
        initGamePhases();
    }

    private void initGamePhases() {
        BaseLib.getUNCPhaseController().registerTickLoop(0, new UNCPhase1());
        BaseLib.getUNCPhaseController().registerTickLoop(1, new UNCPhase2());
        BaseLib.getUNCPhaseController().registerTickLoop(2, new UNCPhase3());
        BaseLib.getUNCPhaseController().registerFinalExpression(() -> {
            Bukkit.broadcastMessage("FIN DU JEU");
        });
    }

    public void initCustomItems() {
        // TODO

        UNCCustomSwordType amethystSword = UNCCustomSwordType.builder("AMETHYST_SWORD")
                .name("Amethyst Sword")
                .lore(new ArrayList<>(List.of("This is a custom sword")))
                .modelData(1)
                .maxDurability(100)
                .attackDamage(10.0)
                .attackSpeed(10)
                .build();

        UNCCustomPickaxeType amethystPickaxe = UNCCustomPickaxeType.builder("AMETHYST_PICKAXE", Material.WOODEN_PICKAXE)
                .name("Amethyst Pickaxe")
                .lore(new ArrayList<>(List.of("This is a custom pickaxe")))
                .modelData(1)
                .maxDurability(100)
                .build();

        CustomItemLib.getUNCCustomItemController().registerCustomItem(amethystSword, amethystPickaxe);
    }

    public void initRecipes() {
        // TODO

        NamespacedKey key = new NamespacedKey(this,"craftAmethystSword");
        ItemStack result = CustomItemLib.getUNCCustomItemController().createCustomItem("AMETHYST_SWORD", 1);
        ShapelessRecipe amethystSword = new ShapelessRecipe(key, result)
                .addIngredient(Material.DIAMOND_SWORD)
                .addIngredient(Material.STICK);

        CustomItemLib.getUNCCustomItemController().registerCraft(amethystSword, null, false);
    }

    @Override
    public void onDisable() {

    }
}
