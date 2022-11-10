package fr.teamunc.uncsurvivals2.metier.models;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.base_unclib.models.tickloops.UNCPhase;
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
        Bukkit.broadcastMessage("Phase 1 start");
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
