package fr.teamunc.uncsurvivals2.helpers;

import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoal;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoalData;
import lombok.var;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemGoalsOperationParserTest {

        @Test
        void parse() {

            var test = new ItemGoalData(Material.AIR, "wow", "TO reward EACH 10 DIVIDEBY 2", 10);
            var parserToTest = new ItemGoalsOperationParser(test);
            var item = new ItemGoal("wow", 20, 0);

            // test 1
            var resultedList = parserToTest.executeOperationAndReturnAmountForEach(item, 1);

            // expected 1
            var expectedReward = 20;
            var expectedResult = new ArrayList<>(List.of(new Tuple<>(1, expectedReward)));

            // asserts 1
            assertEquals(expectedReward, item.getReward());
            assertEquals(expectedResult, resultedList);


            // test 2
            parserToTest.executeOperationAndReturnAmountForEach(item, 10);

            // expected 2
            expectedReward = 10;
            expectedResult = new ArrayList<>(List.of(new Tuple<>(10, expectedReward)));

            // asserts 2
            assertEquals(expectedReward, item.getReward());
            assertEquals(expectedResult, resultedList);

            // test 3
            parserToTest.executeOperationAndReturnAmountForEach(item, 25);

            // expected 3
            expectedReward = 5;
            expectedResult = new ArrayList<>(List.of(new Tuple<>(10, 10), new Tuple<>(10, 10), new Tuple<>(5, expectedReward)));

            // asserts 3
            assertEquals(expectedReward, item.getReward());
            assertEquals(expectedResult, resultedList);

        }
}