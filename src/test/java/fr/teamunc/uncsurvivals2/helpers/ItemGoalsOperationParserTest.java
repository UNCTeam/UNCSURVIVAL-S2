package fr.teamunc.uncsurvivals2.helpers;

import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoal;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoalData;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemGoalsOperationParserTest {

        @Test
        void parse() {

            var test = new ItemGoalData(Material.AIR, "wow", "","TO reward EACH 10 ADD 1", true,10);
            var parserToTest = new ItemGoalsOperationParser(test);
            var item = new ItemGoal("wow", 10, 0);

            // test 1
            var resultedList = parserToTest.executeOperationAndReturnAmountForEach(item, 20);

            // expected 1
            var expectedReward = 12;
            var expectedDeposited = 20;
            var expectedResult = new ArrayList<>(List.of(
                    new Tuple<>(10, 10),
                    new Tuple<>(10, 11)
            ));

            // asserts 1
            assertEquals(expectedReward, item.getReward());
            assertEquals(expectedDeposited, item.getDeposited());
            assertEquals(expectedResult, resultedList);


            // test 2
            item = new ItemGoal("wow", 10, 0);
            resultedList = parserToTest.executeOperationAndReturnAmountForEach(item, 10);

            // expected 2
            expectedReward = 11;
            expectedDeposited = 10;
            expectedResult = new ArrayList<>(List.of(new Tuple<>(10, 10)));

            // asserts 2
            assertEquals(expectedReward, item.getReward());
            assertEquals(expectedDeposited, item.getDeposited());
            assertEquals(expectedResult, resultedList);

            // test 3
            item = new ItemGoal("wow", 10, 0);
            resultedList = parserToTest.executeOperationAndReturnAmountForEach(item, 5);

            // expected 3
            expectedReward = 10;
            expectedDeposited = 5;
            expectedResult = new ArrayList<>(List.of(new Tuple<>(5, expectedReward)));

            // asserts 3
            assertEquals(expectedReward, item.getReward());
            assertEquals(expectedDeposited, item.getDeposited());
            assertEquals(expectedResult, resultedList);

        }
}