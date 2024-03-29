package fr.teamunc.uncsurvivals2.metier.models.itemgoals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.io.Serializable;

// data servant d'initialisation pour les itemgoals
@Getter
@AllArgsConstructor
public class ItemGoalData implements Serializable {
    private Material material;
    private String uniqueName;
    private String displayName;

    /**
     * EACH X : tout les X items
     * REMOVE X : enlever X
     * ADD X : ajouter X
     * SET X : mettre à X
     * DIVIDEBY X : diviser par X
     * MULTIPLYBY X : multiplier par X
     * TO Y : à Y, sachant que Y est une variable de la classe ItemGoal
     * <p>
     * exemple : TO reward EACH 10 ADD 1 MULTIPLEBY 2(ajouter 1 à la variable reward tout les 10 items)
     */
    private String operation;
    private boolean activated;
    private int baseReward;
}
