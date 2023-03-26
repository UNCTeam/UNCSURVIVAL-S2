package fr.teamunc.uncsurvivals2.metier.models.players;

import fr.teamunc.uncsurvivals2.metier.models.players.customInfos.MadnessInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

 @Getter
public class UNCPlayer implements Serializable {
    private final UUID uuid;
    private final MadnessInfo madnessInfo;

    @Builder
    public UNCPlayer(UUID uuid, boolean isDemon) {
        this.uuid = uuid;
        this.madnessInfo = new MadnessInfo(0, isDemon, this);
    }
 }
