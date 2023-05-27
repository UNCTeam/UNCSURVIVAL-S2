package fr.teamunc.uncsurvivals2.helpers;

import fr.teamunc.customitem_unclib.CustomItemLib;
import fr.teamunc.customitem_unclib.models.*;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomBootsType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomChestplateType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomHelmetType;
import fr.teamunc.customitem_unclib.models.customArmors.UNCCustomLeggingsType;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import lombok.var;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

import java.time.Instant;
import java.util.*;

public class ItemInitiator implements IInitialisator{

    private final Map<Material,Material> pocketFurnaceOreIngotMap = new EnumMap<>(
            Map.of(
                    Material.RAW_IRON, Material.IRON_INGOT,
                    Material.RAW_GOLD, Material.GOLD_INGOT,
                    Material.RAW_COPPER, Material.COPPER_INGOT
            )
    );
    @Override
    public void init() {
        //****** pocket items ******//

        UNCCustomActivableType furnaceAccessory = UNCCustomActivableType.builder("POCKET_AUTO_FURNACE")
                .name("Pocket Auto Ores Furnace")
                .lore(new ArrayList<>(List.of("This is a custom activable item that", "can be used to cook all ores in", "the player's inventory")))
                .modelData(3)
                .actionToRun(((itemStack, player) -> {

                    var key = "lastused";

                    var lastusedSTR = CustomItemLib.getUNCCustomItemController().getAdditionalInformations(itemStack, key);

                    var lastUsed = Long.parseLong(lastusedSTR);

                    var now = Instant.now().getEpochSecond();

                    if (now - lastUsed < 20) return;

                    // cook 1 itemStack in the player's inventory
                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item == null || !pocketFurnaceOreIngotMap.containsKey(item.getType())) {
                            continue;
                        }
                        item.setType(pocketFurnaceOreIngotMap.get(item.getType()));
                        CustomItemLib.getUNCCustomItemController().setAdditionalInformations(itemStack, key, String.valueOf(now));
                        break;
                    }


                }))
                .defaultAdditionalInformation(new HashMap<>(Map.of("lastused", 0L)))
                .build();

        UNCCustomActivableType craftingAccessory = UNCCustomActivableType.builder("POCKET_CRAFTING_TABLE")
                .name("Pocket Crafting Table")
                .lore(new ArrayList<>(List.of("This is a custom activable item that", "can be used to open a crafting table")))
                .modelData(2)
                .action(event -> {
                    if (event instanceof PlayerInteractEvent) {
                        PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;

                        if (playerInteractEvent.getClickedBlock() != null && playerInteractEvent.getClickedBlock().getType().isInteractable()) {
                            return 0;
                        }

                        playerInteractEvent.getPlayer().openWorkbench(null, true);
                        return 0;
                    }
                    return 0;
                })
                .build();

        UNCCustomActivableType enderchestAccessory = UNCCustomActivableType.builder("POCKET_ENDER_CHEST")
                .name("Pocket Ender Chest")
                .lore(new ArrayList<>(List.of("This is a custom activable item that", "can be used to open an ender chest")))
                .modelData(4)
                .action(event -> {
                    if (event instanceof PlayerInteractEvent) {
                        PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;

                        if (playerInteractEvent.getClickedBlock() != null && playerInteractEvent.getClickedBlock().getType().isInteractable()) {
                            return 0;
                        }

                        playerInteractEvent.getPlayer().openInventory(playerInteractEvent.getPlayer().getEnderChest());
                        return 0;
                    }
                    return 0;
                })
                .build();

        //****** custom item ******//
        UNCCustomActivableType throwable_camp_structure = UNCCustomActivableType.builder("THROWABLE_CAMP_STRUCTURE")
                .lore(new ArrayList<>(List.of("This is a custom activable item that", "can be used to create a camp structure")))
                .modelData(2)
                .maxDurability(1)
                .action(event -> {
                    if (event instanceof PlayerInteractEvent) {
                        PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;

                        if (playerInteractEvent.getClickedBlock() != null && playerInteractEvent.getClickedBlock().getType().isInteractable()) {
                            return 0;
                        }

                        Location loc = playerInteractEvent.getPlayer().getLocation();

                        // load structure
                        Structure structure = UNCSurvivalS2.get().getServer().getStructureManager().registerStructure(new NamespacedKey(UNCSurvivalS2.get(), "test"),UNCSurvivalS2.get().getServer().getStructureManager().createStructure());
                        structure.place(loc, true, StructureRotation.NONE, Mirror.NONE, -1, 1, new Random());
                        return 1;
                    }
                    return 0;
                }
                ).build();


        CustomItemLib.getUNCCustomItemController().registerCustomItem(
                enderchestAccessory,
                furnaceAccessory,
                craftingAccessory,
                throwable_camp_structure);
    }
}
