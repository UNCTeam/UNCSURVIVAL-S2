package fr.teamunc.uncsurvivals2.minecraft.commands_exec;

import lombok.var;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UncSurvivalCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        switch ( args[0] ) {
            case "npc":
                if (sender instanceof Player) {
                    var name = args[1] != null ? args[1] : "NPC";
                    NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
                    npc.spawn(((Player) sender).getLocation());
                }
                break;
            default:
                sender.sendMessage("default");
                break;
        }

        return true;
    }
}
