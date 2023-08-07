package fr.teamunc.uncsurvivals2.minecraft.commands_exec;

import fr.teamunc.base_unclib.models.libtools.CommandsTab;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UncSurvivalTab extends CommandsTab {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        List<String> result = checkAllTab(
                args,
                Arrays.asList("reloadItemGoalsData", "itemGoalData", "npc"));

        //sort the list
        Collections.sort(result);

        return result;
    }
}
