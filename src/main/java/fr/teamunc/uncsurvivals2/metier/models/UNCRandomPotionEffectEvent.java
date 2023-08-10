package fr.teamunc.uncsurvivals2.metier.models;

import fr.teamunc.base_unclib.models.events.UNCEvent;
import fr.teamunc.base_unclib.utils.helpers.Message;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UNCRandomPotionEffectEvent extends UNCEvent {
    public UNCRandomPotionEffectEvent() {
        super(1, 60000L, new Date(2023 - 1900, Calendar.SEPTEMBER, 9, 18, 0), 10000L);
    }

    private int compteur = 0;

    @Override
    protected void onTick() {

        // tout les 20 ticks
        if (this.tickPassed % 20 != 0) return;

        compteur++;
        Message.Get().broadcastMessageToConsole("Tick " + compteur);
        // random entre plusieurs effets de potions (avec des durées différentes)
        Bukkit.getOnlinePlayers().forEach(player -> player.addPotionEffect(getARandomPotionEffect()));
    }


    private PotionEffect getARandomPotionEffect() {
        var allPossibleEffects = List.of(
                new PotionEffect(PotionEffectType.SPEED, 100, 1),
                new PotionEffect(PotionEffectType.SLOW, 100, 1),
                new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1),
                new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1),
                new PotionEffect(PotionEffectType.HEAL, 100, 1),
                new PotionEffect(PotionEffectType.HARM, 100, 1),
                new PotionEffect(PotionEffectType.JUMP, 100, 1),
                new PotionEffect(PotionEffectType.CONFUSION, 100, 1),
                new PotionEffect(PotionEffectType.REGENERATION, 100, 1),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1),
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 1),
                new PotionEffect(PotionEffectType.WATER_BREATHING, 100, 1),
                new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1),
                new PotionEffect(PotionEffectType.BLINDNESS, 100, 1),
                new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 1),
                new PotionEffect(PotionEffectType.HUNGER, 100, 1),
                new PotionEffect(PotionEffectType.WEAKNESS, 100, 1),
                new PotionEffect(PotionEffectType.POISON, 100, 1),
                new PotionEffect(PotionEffectType.WITHER, 100, 1),
                new PotionEffect(PotionEffectType.HEALTH_BOOST, 100, 1),
                new PotionEffect(PotionEffectType.ABSORPTION, 100, 1),
                new PotionEffect(PotionEffectType.SATURATION, 100, 1),
                new PotionEffect(PotionEffectType.GLOWING, 100, 1),
                new PotionEffect(PotionEffectType.LEVITATION, 100, 1),
                new PotionEffect(PotionEffectType.LUCK, 100, 1),
                new PotionEffect(PotionEffectType.UNLUCK, 100, 1),
                new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 1),
                new PotionEffect(PotionEffectType.CONDUIT_POWER, 100, 1),
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 100, 1),
                new PotionEffect(PotionEffectType.BAD_OMEN, 100, 1),
                new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 100, 1)
        );

        return allPossibleEffects.get((int) (Math.random() * allPossibleEffects.size()));
    }
}
