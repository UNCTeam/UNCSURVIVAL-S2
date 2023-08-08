package fr.teamunc.uncsurvivals2.metier.models.itemgoals;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

// class servant à stocker les données des itemgoals durant la partie
@Getter
@AllArgsConstructor
public class ItemGoal implements Serializable {
    public String uniqueName;
    public int reward;
    public int deposited;
}
