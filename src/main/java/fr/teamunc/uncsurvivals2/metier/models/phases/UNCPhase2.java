package fr.teamunc.uncsurvivals2.metier.models.phases;

import fr.teamunc.base_unclib.models.tickloops.UNCPhase;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;

public class UNCPhase2 extends UNCPhase {
    @Override
    public String getName() {
        return "Phase 2";
    }
    @Override
    public void onTick() {
        UNCSurvivalS2.get().getPlayerController().onTickUNCPlayers(getTick() % 20);
    }

    @Override
    public void onPhaseStart() {
        Bukkit.broadcastMessage("Phase 2 start");
    }

    @Override
    public void onPhaseEnd() {
        Bukkit.broadcastMessage("Phase 2 end");
    }

    @Override
    public boolean isInGamePhase() {
        return true;
    }

    @Override
    public boolean isLastPhase() {
        return false;
    }

    @Override
    public boolean isWithADueDate() {
        return false;
    }

    @Override
    public int getMaxTick() {
        return 200000;
    }

    @Override
    public LocalDateTime getEndingDate() {
        return LocalDateTime.of(2022, 11,8,18,20);
    }
}
