package fr.teamunc.uncsurvivals2.helpers;

import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoal;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoalData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemGoalsOperationParser {
    private ItemGoalData itemGoalData;

    public ItemGoalsOperationParser(ItemGoalData itemGoalData) {
        this.itemGoalData = itemGoalData;
    }

    /**
     * par exemple j'entre amountToDeposite = 50, itemGoal.getDeposited = 40
     * avec l'operation : 'TO reward EACH 50 DIVIDEBY 2', que itemGoal.reward = 100,
     * alors retourne une liste de tuple avec en premier (10, 100) puis (40, 50)
     * signifiant qu'une partie de amountToDeposite (ici 10) vallait 100 de (value entré à TO),
     * puis suite au changement dû à l'operation, l'autre partie ne vallait plus que 50.
     * <hr>
     * <ul>Les opérations possibles sont :
     * <li>TO Y : à Y, sachant que Y est une variable de la classe ItemGoal <b style="color: orange">REQUIRED</b></li>
     * <li>EACH X : tout les X items <b style="color: orange">REQUIRED</b></li>
     * <li>REMOVE X : enlever X</li>
     * <li>ADD X : ajouter X</li>
     * <li>SET X : mettre à X</li>
     * <li>DIVIDEBY X : diviser par X</li>
     * <li>MULTIPLYBY X : multiplier par X</li>
     *  <p>
     *   exemple : TO reward EACH 10 ADD 1 (ajouter 1 à la variable reward tout les 10 items)
     *  </p>
     * </ul>
     * <b style="color: blue">ATTENTION : les opérations doivent être séparées par un espace</b>
     * @param itemGoal l'itemgoal à modifier
     * @param amountToDeposite la quantité à modifier
     * @return une liste de tuple avec en premier la quantité utilisée, et en deuxième la valeur de la variable modifiée
     */
    public List<Tuple<Integer, Integer>> executeOperationAndReturnAmountForEach(ItemGoal itemGoal, int amountToDeposite) {

        // split l'operation en plusieurs parties
        String[] operations = itemGoalData.getOperation().split(" ");

        // Liste pour stocker les tuples
        List<Tuple<Integer, Integer>> results = new ArrayList<>();

        // si l'operation n'a pas au moins 4 arguments (TO et EACH présents + leurs valeurs)
        if (operations.length < 6) {
            throw new IllegalArgumentException("Operation must have at least 6 arguments with TO and EACH present spaced by a space and at least one operator");
        }

        Field fieldToOperate;
        int valueToOperate;

        // récupère la valeur de la variable à modifier (TO)
        if (Objects.equals(operations[0], "TO")) {
            String variableName = operations[1];
            try {
                fieldToOperate = itemGoal.getClass().getDeclaredField(variableName);
                valueToOperate = fieldToOperate.getInt(itemGoal);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return results;
            }
        } else {
            throw new IllegalArgumentException("Operation must start with TO");
        }
        // remove TO part of the operations
        String[] operationsValuesToUse = Arrays.copyOfRange(operations, 2, operations.length);

        // execute l'operation pour chaque X (EACH)
        if (Objects.equals(operations[2], "EACH")) {

            // remove EACH part of the operations
            operationsValuesToUse = Arrays.copyOfRange(operationsValuesToUse, 2, operationsValuesToUse.length);

            // récupère la valeur de X, Z et Y
            int each = Integer.parseInt(operations[3]);
            int remainingAmount = amountToDeposite;
            int currentDeposited = itemGoal.getDeposited();

            // tant qu'il reste de la quantité à "déposer" (non utilisée par l'opération, car on ne dépose pas vraiment ici)
            while (remainingAmount > 0) {
                // calculer A
                int nextMultiple = ((currentDeposited / each) + 1) * each;
                int requiredAmountToNextMultiple = nextMultiple - currentDeposited;
                int amountUsed;

                // si Z < A
                if (remainingAmount < requiredAmountToNextMultiple) {
                    amountUsed = remainingAmount;
                    results.add(new Tuple<>(amountUsed, valueToOperate));
                } else { // si Z >= A
                    amountUsed = requiredAmountToNextMultiple;
                    results.add(new Tuple<>(amountUsed, valueToOperate));

                    valueToOperate = parseOperation(operationsValuesToUse, valueToOperate);
                }

                // met a jour pour l item goal
                currentDeposited += amountUsed;
                remainingAmount -= amountUsed;
            }

            // met a jour l item goal
            try {
                fieldToOperate.setInt(itemGoal, valueToOperate);
                itemGoal.deposited = currentDeposited;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("Operation must have EACH present after TO");
        }

        return results;
    }


    private int parseOperation(String[] operations, int valueToOperate) {
        var res = 0;

        switch (operations[0]) {
            case "REMOVE":
                res = valueToOperate - Integer.parseInt(operations[1]);
                break;
            case "ADD":
                res = valueToOperate + Integer.parseInt(operations[1]);
                break;
            case "SET":
                res = Integer.parseInt(operations[1]);
                break;
            case "DIVIDEBY":
                res = valueToOperate / Integer.parseInt(operations[1]);
                break;
            case "MULTIPLYBY":
                res = valueToOperate * Integer.parseInt(operations[1]);
                break;
            default:
                throw new IllegalArgumentException("Operation must have a valid operation after EACH");
        }

        if (operations.length > 2) {
            String[] operationsValuesToUse = Arrays.copyOfRange(operations, 2, operations.length);
            return parseOperation(operationsValuesToUse, res);
        } else {
            return res;
        }

    }
}
