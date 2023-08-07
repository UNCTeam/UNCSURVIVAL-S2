package fr.teamunc.uncsurvivals2.minecraft.commands_exec;

import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoal;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoalData;
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
                if (sender instanceof Player player) {
                    var name = args[1] != null ? args[1] : "NPC";
                    NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
                    npc.spawn(player.getLocation());
                }
                break;

            case "itemGoalData":
                if (sender instanceof Player player) {
                    var item = player.getInventory().getItemInMainHand();

                    var itemGoalData = new ItemGoalData(item.getType(), item.getType().name(),item.getItemMeta().getDisplayName(),"TO reward EACH 10 ADD 1", true,10);
                    UNCSurvivalS2.get().getItemGoalsController().addItemGoalData(itemGoalData);

                    var itemGoal = new ItemGoal(itemGoalData.getUniqueName(), 10, 0);
                    UNCSurvivalS2.get().getItemGoalsController().addItemGoal(itemGoal);

                }
                break;
            case "reloadItemGoalsData":

                try {
                    UNCSurvivalS2.get().getItemGoalsController().reloadItemsGoal();
                    Message.Get().sendMessage("Item goals reloaded from data", sender, false);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    sender.sendMessage("Error reloadItemGoalsData");
                }
                break;
            default:
                sender.sendMessage("default");
                break;
        }

        return true;
    }
}
