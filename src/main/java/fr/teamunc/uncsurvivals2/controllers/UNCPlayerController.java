package fr.teamunc.uncsurvivals2.controllers;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import fr.teamunc.uncsurvivals2.metier.models.players.UNCPlayer;
import fr.teamunc.uncsurvivals2.metier.models.players.UNCPlayersContainer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UNCPlayerController {
    private final UNCPlayersContainer playersContainer;

    private BukkitRunnable eachTickTask;
    public UNCPlayerController() {
        this.playersContainer = initPlayersContainer();
        Message.Get().broadcastMessageToConsole("[PlayersController] : Loading " + getPlayers().size() + " players data");

        // start each tick task
        this.eachTickTask = new BukkitRunnable() {
            @Override
            public void run() {
                eachTick();
            }
        };
        this.eachTickTask.runTaskTimer(UNCSurvivalS2.get(), 0, 10);
    }

    public void resetPlayers() {
        this.playersContainer.getPlayers().clear();
    }

    public UNCPlayersContainer initPlayersContainer() {
        try {
            return UNCEntitiesContainer.loadContainer("players", UNCPlayersContainer.class);
        } catch (FileNotFoundException e) {
            UNCSurvivalS2.get().getLogger().info("Creating new unc players container file");
            return new UNCPlayersContainer();
        }
    }

    public List<UNCPlayer> getPlayers() {
        return this.playersContainer.getPlayers();
    }

    public void addPlayer(UNCPlayer player) {
        this.getPlayers().add(player);
    }

    public void removePlayer(UNCPlayer player) {
        this.getPlayers().remove(player);
    }

    public void onTickUNCPlayers(int tickInSecond) {
        // actualise madness display bar
        this.getPlayers().forEach(uncPlayer -> uncPlayer.getMadnessInfo().actualiseMadnessDisplayBar());

        // apply madness effects only each 10 ticks
        if (tickInSecond == 0) {
            this.getPlayers().forEach(uncPlayer -> uncPlayer.getMadnessInfo().applyMadnessEffects());
        }
    }

    private void eachTick() {
        // add players if isn't already in the container
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (this.getPlayers().stream().noneMatch(uncPlayer -> uncPlayer.getUuid().equals(player.getUniqueId()))) {
                this.addPlayer(
                        UNCPlayer.builder()
                        .uuid(player.getUniqueId())
                        .isDemon(false)
                        .build()
                );
            }
        });
    }

    public void playerMadnessIncreaseRandomly(UUID playerUUID, int amount) {
        // 1 chance out of 3 to increase madness
        // only in phase 2 or more
        if (Math.random() < 0.33 && BaseLib.getUNCPhaseController().getPhaseNumber() > 0) {
            Optional<UNCPlayer> player = this.getPlayers().stream().filter(uncPlayer -> uncPlayer.getUuid().equals(playerUUID)).findFirst();
            if (player.isPresent() && !player.get().getMadnessInfo().isDemon()) {
                player.get().getMadnessInfo().addMadness(amount);
            }
        }
    }

    public void save() {
        this.playersContainer.save("players");
    }
}
