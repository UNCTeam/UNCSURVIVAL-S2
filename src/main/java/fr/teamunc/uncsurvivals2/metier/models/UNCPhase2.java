package fr.teamunc.uncsurvivals2.metier.models;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.base_unclib.models.tickloops.UNCPhase;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;

public class UNCPhase2 extends UNCPhase {
    @Override
    public String getName() {
        return "Phase 2";
    }
    @Override
    public void onTick() {
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
        return true;
    }

    @Override
    public int getMaxTick() {
        return 0;
    }

    @Override
    public LocalDateTime getEndingDate() {
        return LocalDateTime.of(2022, 11,8,18,20);
    }
}
