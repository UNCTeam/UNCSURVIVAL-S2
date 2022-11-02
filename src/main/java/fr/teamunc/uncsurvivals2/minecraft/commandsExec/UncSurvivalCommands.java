package fr.teamunc.uncsurvivals2.minecraft.commandsExec;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UncSurvivalCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("uncchest")) {
            return false;
        }

        if(sender instanceof Player) {
            Player player = (Player) sender;
            UUID chestUUID = UNCSurvivalS2.Get().getPlayerEnderChestContainer().getCustomEnderchest().get(player.getUniqueId());
            if(chestUUID == null) {
                chestUUID = BaseLib.getUNCInventoryController().registerPersistantInventory("test2");
                UNCSurvivalS2.Get().getPlayerEnderChestContainer().getCustomEnderchest().put(player.getUniqueId(), chestUUID);
            }
            player.openInventory(BaseLib.getUNCInventoryController().getPersistantInventory(chestUUID).getInventory());
        }
        return true;
    }
}
