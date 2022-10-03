package fr.teamunc.uncsurvivals2.metier.models;

import fr.teamunc.base_unclib.models.tickloops.UNCPhase;
import org.bukkit.Bukkit;

public class UNCPhase3 extends UNCPhase {
    @Override
    public void onTick() {
        Bukkit.broadcastMessage("Phase 3");
    }

    @Override
    public void onPhaseStart() {
        Bukkit.broadcastMessage("Phase 3 start");
    }

    @Override
    public void onPhaseEnd() {
        Bukkit.broadcastMessage("Phase 3 end");
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
    public int getMaxTick() {
        return 10;
    }
}
