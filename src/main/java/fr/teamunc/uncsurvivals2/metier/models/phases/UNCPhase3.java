package fr.teamunc.uncsurvivals2.metier.models.phases;

import fr.teamunc.base_unclib.models.tickloops.UNCPhase;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;

public class UNCPhase3 extends UNCPhase {
    @Override
    public String getName() {
        return "Phase 3";
    }

    @Override
    public void onTick() {
        UNCSurvivalS2.get().getPlayerController().onTickUNCPlayers(getTick() % 20);
    }

    @Override
    public void onPhaseStart() {
        Bukkit.broadcastMessage("Phase 3 start");
    }

    @Override
    public void onPhaseEnd() {
        Bukkit.broadcastMessage("Phase 3 end");

        Bukkit.broadcastMessage("FIN DU JEU");
    }

    @Override
    public boolean isInGamePhase() {
        return false;
    }

    @Override
    public boolean isLastPhase() {
        return true;
    }

    @Override
    public boolean isWithADueDate() {
        return true;
    }

    @Override
    public int getMaxTick() {
        return 0;
    }

    @Override
    public LocalDateTime getEndingDate() {
        return LocalDateTime.of(2022, 11,11,19,20);
    }
}
