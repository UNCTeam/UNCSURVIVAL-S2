package fr.teamunc.uncsurvivals2;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.base_unclib.Base_UNCLib;
import fr.teamunc.base_unclib.models.inventories.CancelSlot;
import fr.teamunc.base_unclib.models.inventories.UNCInventory;
import fr.teamunc.base_unclib.models.inventories.UNCItemMenu;
import fr.teamunc.uncsurvivals2.minecraft.commandsExec.UncSurvivalCommands;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
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
        BaseLib.init(this);

        // Init interfaces
        this.initInterfaces();
        // init test lib
        this.getCommand("test").setExecutor(new UncSurvivalCommands());
    }

    public void initInterfaces() {
        // init interfaces
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§b§lTest");
        itemMeta.setLore(Arrays.asList("§7Cliquez pour tester"));
        item.setItemMeta(itemMeta);

        ItemStack itemClick = new ItemStack(Material.ARROW);
        ItemMeta itemMeta2 = item.getItemMeta();
        itemMeta.setDisplayName("§b§lOuvrir un inventaire");
        itemMeta.setLore(Arrays.asList("§7Cliquez pour tester"));
        item.setItemMeta(itemMeta);

        UNCInventory inventory = UNCInventory.builder("test", "§b§lTest", 9)
                .cancelSlots(new CancelSlot[]{new CancelSlot(0,9)})
                .fixedItems(Arrays.asList(new UNCItemMenu(5, itemClick, event -> {
                    BaseLib.getUNCInventoryController().getInventories().get("test2")
                            .openInventory((Player) event.getWhoClicked());
                })))
                .build();

        UNCInventory inventory2 = UNCInventory.builder("test2", "§b§lTest2", 54)
                .fixedItems(Arrays.asList(
                        new UNCItemMenu(0, new ItemStack(Material.AMETHYST_BLOCK), null),
                        new UNCItemMenu(1, item, event -> {
                            event.getWhoClicked().sendMessage("test");
                        }),
                        new UNCItemMenu(2, new ItemStack(Material.GOLD_BLOCK), event -> {
                        })
                ))
                .closeAction(event -> {
                    event.getPlayer().sendMessage("closing inventory");
                })
                .cancelSlots(new CancelSlot[]{new CancelSlot(0, 1, 2)})
                .build();

        BaseLib.getUNCInventoryController().registerInventory(inventory, inventory2);
    }

    @Override
    public void onDisable() {

    }
}
