package fr.teamunc.uncsurvivals2.metier.models.players;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

 @Getter
public class UNCPlayer implements Serializable {
    private final UUID uuid;

    @Builder
    public UNCPlayer(UUID uuid) {
        this.uuid = uuid;
    }
 }
