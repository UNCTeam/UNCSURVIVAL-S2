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
import fr.teamunc.uncsurvivals2.controllers.UNCPlayerController;
import fr.teamunc.uncsurvivals2.metier.models.phases.UNCPhase1;
import fr.teamunc.uncsurvivals2.metier.models.phases.UNCPhase2;
import fr.teamunc.uncsurvivals2.metier.models.phases.UNCPhase3;
import fr.teamunc.uncsurvivals2.minecraft.commands_exec.UncSurvivalCommands;
import fr.teamunc.uncsurvivals2.minecraft.eventsListeners.PlayerListener;
import fr.teamunc.zone_unclib.ZoneLib;
import lombok.Getter;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
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
        initCustomItems();
        initCustomBlocks();
        initRecipes();
        initScoreboard();
        initController();
        initCommandsAndListeners();

        // init game phases
        initGamePhases();
    }

    private void initCommandsAndListeners() {
        // Commands
        this.getCommand("test").setExecutor(new UncSurvivalCommands());

        // Listeners
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private void initController() {
        this.playerController = new UNCPlayerController();
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

        UNCCustomSwordType strangeSword = UNCCustomSwordType.builder("STRANGE_SWORD")
                .name("Strange Sword")
                .lore(new ArrayList<>(List.of(
                        "This sword is a ancient artifact...",
                        "found in the depths of the earth",
                        "it is said that it has the power",
                        "to health demons by draining living's",
                        "life force",
                        "",
                        "§r§6§lUtility:",
                        "§r§8- §eEat a player's health")))
                .modelData(2)
                .maxDurability(432)
                .attackDamage(1.0)
                .action((Event event) -> {
                    if (event instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                        if (e.getEntity() instanceof Player) {
                            Player p = (Player) e.getDamager();
                            p.setFoodLevel(Math.min(p.getFoodLevel() + 4,20));
                            return 10;
                        }
                    }
                    return 0;
                })
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
                amethystFood,
                strangeSword);
    }

    public void initCustomBlocks() {
        UNCCustomBlockType growthBlock = UNCCustomBlockType.builder("GROWTH_BLOCK")
                .name("Growth Block")
                .lore(new ArrayList<>(List.of("This is a custom block")))
                .modelData(1)
                .blockMaterial(Material.STONE)
                .dropOnlyIfCorrectToolUsed(true)
                .droppedItem(new ItemStack(Material.DIAMOND))
                .build();

        UNCCustomBlockType streetlightBlock = UNCCustomBlockType.builder("STREET_LIGHT_BLOCK")
                .name("street light Block")
                .lore(new ArrayList<>(List.of("This is a street light")))
                .modelData(2)
                .instantBreak(true)
                .droppedItem(new ItemStack(Material.DIAMOND))
                .build();

        UNCCustomBlockType fleshEater = UNCCustomBlockType.builder("FLESH_EATER")
                .name("Flesh Eater Block")
                .lore(new ArrayList<>(List.of("This is a flesh eater")))
                .modelData(4)
                .instantBreak(true)
                .defaultAdditionalInformation(new HashMap<>(Map.of("destinationSet", false)))
                .actionRunnable(block -> {
                    // play particles
                    block.getLocation().getWorld().spawnParticle(
                            Particle.ASH,
                            block.getLocation().clone().add(0.5, 1, 0.5),
                            20,
                            0.25,
                            0.25,
                            0.25,
                            0.1);
                })
                .action((event, uncCustomBlock) -> {
                    if (event instanceof BlockPlaceEvent) {
                        BlockPlaceEvent blockPlaceEvent = (BlockPlaceEvent) event;
                        Message.Get().broadcastMessageToEveryone(blockPlaceEvent.getPlayer().getName());
                    }
                })
                .build();

        UNCCustomBlockType foreuseBlock = UNCCustomBlockType.builder("FOREUSE_BLOCK")
                .name("Foreuse Block")
                .lore(new ArrayList<>(List.of("This is a foreuse")))
                .modelData(3)
                .instantBreak(true)
                .droppedItem(new ItemStack(Material.DIAMOND))
                .actionRunnable((block) -> {

                    // break block in an area of 3x3 blocks at the Y level of the block
                    Location blockLocation = block.getLocation();
                    double yLevelToAdd = block.getAdditionalInformation("Y", Double.class);

                    if (blockLocation.getY() - yLevelToAdd < -63) {
                        block.setAdditionalInformation("Y", 1.0);
                        return;
                    }

                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                Location location = new Location(
                                        blockLocation.getWorld(),
                                        blockLocation.getX() + x,
                                        blockLocation.getY() - yLevelToAdd,
                                        blockLocation.getZ() + z);
                                // drop items at the top of the main block (the block that has been placed)
                                for (ItemStack itemStack : location.getBlock().getDrops()) {
                                    block.getLocation().getWorld().dropItemNaturally(block.getLocation().clone().add(0,1,0),itemStack);
                                }
                                location.getBlock().setType(Material.AIR);
                            }
                        }
                    }

                    block.setAdditionalInformation("Y", yLevelToAdd + 1.0);
                })
                .addBaseAdditionalData("Y", 1.0)
                .build();

        CustomBlockLib.getCustomBlockController().registerCustomBlock(growthBlock, streetlightBlock, foreuseBlock, fleshEater);
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
        // Plugin shutdown logic
        this.getPlayerController().save();
    }
}
