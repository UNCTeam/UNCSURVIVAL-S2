package fr.teamunc.uncsurvivals2.metier.models.players.customInfos;

import fr.teamunc.uncsurvivals2.metier.models.players.UNCPlayer;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.Serializable;

@Getter @Setter
public class MadnessInfo implements Serializable {
    private int madness;
    private boolean isDemon;
    private final UNCPlayer owner;
    public static final int MAX_MADNESS = 20;

    public MadnessInfo(int madness, boolean isDemon, UNCPlayer uncPlayer) {
        this.madness = madness;
        this.isDemon = isDemon;
        this.owner = uncPlayer;
    }

    public void addMadness(int madness) {
        this.setMadness(this.getMadness() + madness);
    }

    public void removeMadness(int madness) {
        this.setMadness(this.getMadness() - madness);
    }

    public void setMadness(int madness) {
        this.madness = madness;
        if (this.madness > MAX_MADNESS) {
            this.madness = MAX_MADNESS;
        }
        if (this.madness < 0) {
            this.madness = 0;
        }
    }

    public void actualiseMadnessDisplayBar() {
        String madnessType = this.isDemon ? "demon" : "normal";

        Player player = Bukkit.getPlayer(this.owner.getUuid());
        if (player != null && player.isOnline()) {
            madnessType += player.isInWater() ? ".inwater." : ".";

            String actionBarText = "madness." + madnessType + this.getMadness();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TranslatableComponent(actionBarText));

        }
    }

    public void applyMadnessEffects() {
        // give a speed boost if madness is high
        if (this.getMadness() > 10) {
            Player player = Bukkit.getPlayer(this.owner.getUuid());
            if (player != null
                    && player.isOnline()
                    && player.getGameMode() != GameMode.CREATIVE
                    && player.getGameMode() != GameMode.SPECTATOR) {
                if (this.getMadness() > 13 && Math.random() < 0.2) {
                    player.setSneaking(false);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 5));
                }
                if (this.getMadness() > 16 && Math.random() < 0.1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 40, 10));
                }

                if (this.getMadness() > 18 && Math.random() < 0.3) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 40, 10));
                }

                if (this.getMadness() > 19 && Math.random() < 0.3) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 10));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 60, 10));

                }
            }
        }
    }
}
