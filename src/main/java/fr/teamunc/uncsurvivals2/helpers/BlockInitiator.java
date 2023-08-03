package fr.teamunc.uncsurvivals2.helpers;

import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.customblock_unclib.CustomBlockLib;
import fr.teamunc.customblock_unclib.models.UNCCustomBlockType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BlockInitiator implements IInitialisator{
    @Override
    public void init() {
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
                .actionToRunOnPlace((uncCustomBlock, player) -> {
                    uncCustomBlock.setAdditionalInformation("PlayerUUID", player.getUniqueId());
                })
                .actionRunnable(block -> {

                    // break the block as a player
                    Player player = Bukkit.getPlayer(block.getAdditionalInformation("PlayerUUID", UUID.class));

                    if (player == null) return;

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
                                    if (player.breakBlock(location.getBlock()))
                                        block.getLocation().getWorld().dropItemNaturally(block.getLocation().clone().add(0,1,0),itemStack);
                                    else return;
                                }

                            }
                        }
                    }

                    block.setAdditionalInformation("Y", yLevelToAdd + 1.0);
                })
                .addBaseAdditionalData("Y", 1.0)
                .addBaseAdditionalData("PlayerUUID", null)
                .build();

        CustomBlockLib.getCustomBlockController().registerCustomBlock(growthBlock, streetlightBlock, foreuseBlock, fleshEater);
    }
}
