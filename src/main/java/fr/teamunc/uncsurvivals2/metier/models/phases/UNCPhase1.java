package fr.teamunc.uncsurvivals2.metier.models.phases;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.base_unclib.models.tickloops.UNCPhase;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;

public class UNCPhase1 extends UNCPhase {
    @Override
    public String getName() {
        return "Phase 1";
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onPhaseStart() {
        UNCSurvivalS2.get().getPlayerController().resetPlayers();
    }

    @Override
    public void onPhaseEnd() {
        Bukkit.broadcastMessage("Phase 1 end");
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
        return 20 * 5;
    }

    @Override
    public LocalDateTime getEndingDate() {
        return null;
    }
}
