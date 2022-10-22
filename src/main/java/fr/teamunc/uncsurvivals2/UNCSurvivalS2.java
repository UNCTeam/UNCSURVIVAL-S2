package fr.teamunc.uncsurvivals2;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.customitem_unclib.CustomItemLib;
import fr.teamunc.customitem_unclib.controllers.UNCCustomItemController;
import fr.teamunc.customitem_unclib.models.*;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomBootsType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomChestplateType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomHelmetType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomLeggingsType;
import fr.teamunc.ekip_unclib.EkipLib;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase1;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase2;
import fr.teamunc.uncsurvivals2.metier.models.UNCPhase3;
import fr.teamunc.uncsurvivals2.minecraft.commandsExec.UncSurvivalCommands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
                .action(event -> {
                    if (event instanceof BlockBreakEvent) {
                        BlockBreakEvent blockBreakEvent = (BlockBreakEvent) event;
                        ItemStack item = blockBreakEvent.getPlayer().getInventory().getItemInMainHand();
                        // break block in an area of 3x3x3 blocks of the same block
                        Location blockLocation = blockBreakEvent.getBlock().getLocation();
                        for (int x = -1; x <= 1; x++) {
                            for (int y = -1; y <= 1; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    Location location = new Location(blockLocation.getWorld(), blockLocation.getX() + x, blockLocation.getY() + y, blockLocation.getZ() + z);
                                    if (!blockLocation.equals(location) && location.getBlock().getType() == blockBreakEvent.getBlock().getType()) {
                                        location.getBlock().breakNaturally(item);
                                    }
                                }
                            }
                        }

                    }
                    return 0;
                })
                .maxDurability(100)
                .build();

        UNCCustomStaticType amethyst = UNCCustomStaticType.builder("AMETHYST_INGOT")
                .name("Amethyst Ingot")
                .lore(new ArrayList<>(List.of("This is a custom static item")))
                .modelData(1)
                .build();

        UNCCustomActivableType amethystActivable = UNCCustomActivableType.builder("AMETHYST_ACTIVABLE")
                .name("Amethyst Activable")
                .lore(new ArrayList<>(List.of("This is a custom activable item")))
                .modelData(1)
                .action(event -> {
                    if (event instanceof PlayerInteractEvent) {
                        PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;

                        if (playerInteractEvent.getClickedBlock() != null && playerInteractEvent.getClickedBlock().getType().isInteractable()) {
                            return 0;
                        }

                        Item item = playerInteractEvent.getPlayer().getWorld().dropItem(playerInteractEvent.getPlayer().getLocation(), new ItemStack(Material.DIAMOND));
                        item.setPickupDelay(0);
                        return 1;
                    }
                    return 0;
                })
                .maxDurability(2)
                .build();

        UNCCustomChestplateType amethystChestplate = UNCCustomChestplateType.builder("AMETHYST_CHESTPLATE")
                .name("Amethyst Chestplate")
                .lore(new ArrayList<>(List.of("This is a custom chestplate")))
                .modelData(1)
                .armor(20)
                .armorToughness(20)
                .knockbackResistance(20)
                .maxDurability(1)
                .build();

        UNCCustomHelmetType amethystHelmet = UNCCustomHelmetType.builder("AMETHYST_HELMET")
                .name("Amethyst Helmet")
                .lore(new ArrayList<>(List.of("This is a custom helmet")))
                .modelData(1)
                .armor(20)
                .armorToughness(20)
                .knockbackResistance(20)
                .maxDurability(1)
                .build();

        UNCCustomLeggingsType amethystLeggings = UNCCustomLeggingsType.builder("AMETHYST_LEGGINGS")
                .name("Amethyst Leggings")
                .lore(new ArrayList<>(List.of("This is a custom leggings")))
                .modelData(1)
                .armor(20)
                .armorToughness(20)
                .knockbackResistance(20)
                .maxDurability(1)
                .build();

        UNCCustomBootsType amethystBoots = UNCCustomBootsType.builder("AMETHYST_BOOTS")
                .name("Amethyst Boots")
                .lore(new ArrayList<>(List.of("This is a custom boots")))
                .modelData(1)
                .armor(20)
                .armorToughness(20)
                .knockbackResistance(20)
                .maxDurability(1)
                .build();

        UNCCustomFoodType amethystFood = UNCCustomFoodType.builder("AMETHYST_FOOD", Material.APPLE)
                .name("Amethyst Food")
                .lore(new ArrayList<>(List.of("This is a custom food")))
                .modelData(1)
                .foodLevel(20)
                .saturation(20)
                .build();

        CustomItemLib.getUNCCustomItemController().registerCustomItem(
                amethystSword,
                amethystPickaxe,
                amethyst,
                amethystActivable,
                amethystChestplate,
                amethystHelmet,
                amethystLeggings,
                amethystBoots,
                amethystFood);
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
