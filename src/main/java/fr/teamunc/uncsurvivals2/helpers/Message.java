package fr.teamunc.uncsurvivals2.helpers;


import fr.teamunc.uncsurvivals2.metier.TickAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Message {

    // SINGLETON
    private static Message instance;
    private Message(){
        super();
    }
    public static Message Get() {
        if (instance == null) instance = new Message();
        return instance;
    }
    // END SINGLETON

    private final String prefix = "§c§l[ UNC ] §r: ";

    public void broadcastMessageToEveryone(String message) {
        Bukkit.broadcastMessage(prefix + message);
    }

    public void broadcastMessageToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + message);
    }

    public void broadcastMessageToPlayerUUID(String message, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) player.sendMessage(prefix + message);
    }

    public void broadcastMessageToPlayersUUID(String message, ArrayList<UUID> uuids) {
        for (UUID uuid : uuids) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) player.sendMessage(prefix + message);
        }
    }
}
