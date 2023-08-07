package fr.teamunc.uncsurvivals2.controllers;

import fr.teamunc.base_unclib.controllers.IUNCController;
import fr.teamunc.base_unclib.models.jsonEntities.UNCEntitiesContainer;
import fr.teamunc.base_unclib.utils.helpers.Message;
import fr.teamunc.uncsurvivals2.UNCSurvivalS2;
import fr.teamunc.uncsurvivals2.helpers.ItemGoalsOperationParser;
import fr.teamunc.uncsurvivals2.helpers.Tuple;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoal;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoalData;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoalsInGameContainer;
import fr.teamunc.uncsurvivals2.metier.models.itemgoals.ItemGoalsInitDataContainer;
import org.bukkit.inventory.ItemStack;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemGoalsController implements IUNCController {

    private final ItemGoalsInGameContainer itemGoalsInGameContainer;
    private static final String ITEM_GOALS_IN_GAME_CONTAINER_PATH = "itemGoalsInGame";
    private ItemGoalsInitDataContainer itemGoalsInitDataContainer;
    private static final String ITEM_GOALS_INIT_DATA_CONTAINER_PATH = "itemGoalsInitData";

    public ItemGoalsController() {

        // init item goal data for base information of items goal
        this.itemGoalsInitDataContainer = initItemGoalsInitDataContainer();
        Message.Get().broadcastMessageToConsole("[ItemGoalsController] : Loading " + itemGoalsInitDataContainer.getItems().length + " items goals data");

        // use items goal data for checking if any new items goals need to be added or removed from the game
        this.itemGoalsInGameContainer = initItemGoalsInGameContainer(this.itemGoalsInitDataContainer.getItems());
    }

    private ItemGoalsInGameContainer initItemGoalsInGameContainer(ItemGoalData[] itemGoalDataList) {
        try {
            var savedListOfInGameItemGoals = UNCEntitiesContainer.loadContainer(ITEM_GOALS_IN_GAME_CONTAINER_PATH, ItemGoalsInGameContainer.class);

            this.initAndCheckAllItemFromItemGoalsDataToInGameItemGoals(itemGoalDataList, savedListOfInGameItemGoals);

            return savedListOfInGameItemGoals;
        } catch (FileNotFoundException e) {
            UNCSurvivalS2.get().getLogger().info("Creating new unc itemGoalsMap container file");
            return new ItemGoalsInGameContainer();
        }
    }

    private ItemGoalsInitDataContainer initItemGoalsInitDataContainer() {
        try {
            return UNCEntitiesContainer.loadContainer(ITEM_GOALS_INIT_DATA_CONTAINER_PATH, ItemGoalsInitDataContainer.class);
        } catch (FileNotFoundException e) {
            UNCSurvivalS2.get().getLogger().info("Creating new unc itemGoalsInitData container file");
            return new ItemGoalsInitDataContainer();
        }
    }

    /**
     * reload item goals data from files but not in game, inGames item goals are saved to file
     */
    public void reloadItemsGoal() {
        // save in game item goals
        this.itemGoalsInGameContainer.save(ITEM_GOALS_IN_GAME_CONTAINER_PATH);

        // reload item goals data
        this.itemGoalsInitDataContainer = initItemGoalsInitDataContainer();
        ItemGoalData[] itemGoalDataList = this.itemGoalsInitDataContainer.getItems();
        Message.Get().broadcastMessageToConsole("[ItemGoalsController] : Loading " + itemGoalsInitDataContainer.getItems().length + " items goals data");

        // generate new items goals in game from item goals data
        this.initAndCheckAllItemFromItemGoalsDataToInGameItemGoals(itemGoalDataList, this.itemGoalsInGameContainer);

    }

    private void initAndCheckAllItemFromItemGoalsDataToInGameItemGoals(ItemGoalData[] itemGoalDataList, ItemGoalsInGameContainer savedListOfInGameItemGoals) {

        // guard clause if any name are duplicated
        if (Arrays.stream(itemGoalDataList)
                .anyMatch(itemGoalData -> Arrays.stream(itemGoalDataList)
                        .anyMatch(itemGoalData1 ->
                                itemGoalData != itemGoalData1
                                        && itemGoalData.getUniqueName().equals(itemGoalData1.getUniqueName())))) {
            throw new IllegalArgumentException("Item goal unique name must be unique");
        }


        // adding
        var addedNb = 0;
        for (ItemGoalData itemGoalData : itemGoalDataList) {
            if (savedListOfInGameItemGoals
                    .getItems()
                    .stream()
                    .noneMatch(itemGoal -> Objects.equals(itemGoal.uniqueName, itemGoalData.getUniqueName()))) {

                var itemGoal = new ItemGoal(
                        itemGoalData.getUniqueName(),
                        itemGoalData.getBaseReward(),
                        0);
                addedNb++;
                savedListOfInGameItemGoals.getItems().add(itemGoal);
            }
        }

        if (addedNb > 0) {
            Message.Get().broadcastMessageToConsole("[ItemGoalsController] : " + addedNb + " new item goals added");
        }
    }

    /**
     * @return a list of all activated item goals according to the item goal data
     */
    public List<ItemGoal> getActivatedItemGoals() {
        return this.itemGoalsInGameContainer.getItems().stream()
                .filter(itemGoal -> this.isItemGoalActivated(itemGoal.uniqueName))
                .toList();
    }

    public List<ItemStack> getActivatedItemGoalsAsItemStack() {
        var activatedItemGoals = getActivatedItemGoals();
        var res = new ArrayList<ItemStack>();

        for (ItemGoal itemGoal : activatedItemGoals) {
            var itemGoalData = getItemGoalData(itemGoal.uniqueName);
            var itemStack = new ItemStack(itemGoalData.getMaterial(), 1);
            var itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(itemGoalData.getDisplayName());
                itemMeta.setLore(generateLoreForGivenItemGoal(itemGoal));

                itemStack.setItemMeta(itemMeta);
            }
            res.add(itemStack);
        }

        return res;
    }

    private List<String> generateLoreForGivenItemGoal(ItemGoal itemGoal) {
        var itemGoalData = getItemGoalData(itemGoal.uniqueName);
        var res = new ArrayList<String>();

        res.add("§6§lActual/Max reward : §7§l" + itemGoal.getReward() + "§6§l/§7§l" + itemGoalData.getBaseReward());
        res.add("§6§lAlready Deposited : §7§l" + itemGoal.getDeposited());

        return res;
    }

    public boolean isItemGoalActivated(String uniqueName) {
        return getItemGoalData(uniqueName).isActivated();
    }

    public ItemGoalData getItemGoalData(String uniqueName) {
        return Arrays.stream(this.itemGoalsInitDataContainer.getItems())
                .filter(itemGoalData -> itemGoalData.getUniqueName().equals(uniqueName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item goal data unique name not found"));
    }

    /**
     * @param uniqueName unique name of the item goal
     * @return the item goal in game, if not found or not activated throw an exception
     */
    public ItemGoal getItemGoalInGame(String uniqueName) {
        return getActivatedItemGoals().stream()
                .filter(itemGoal -> itemGoal.uniqueName.equals(uniqueName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item goal unique name not found or not activated"));
    }

    /**
     * @param uniqueName unique name of the item goal
     * @param amountToDeposit amount to deposit
     * @return a tuple list of the number of itemgoals deposited for the given reward amount
     */
    public synchronized List<Tuple<Integer, Integer>> depositItemGoal(String uniqueName, int amountToDeposit) {

        var itemGoalData = getItemGoalData(uniqueName);
        var itemGoalInGame = getItemGoalInGame(uniqueName);


        ItemGoalsOperationParser itemGoalsOperationParser = new ItemGoalsOperationParser(itemGoalData);
        return itemGoalsOperationParser.executeOperationAndReturnAmountForEach(itemGoalInGame, amountToDeposit);
    }

    public void addItemGoalData(ItemGoalData itemGoalData) {
        var newList = new ArrayList<>(Arrays.asList(this.itemGoalsInitDataContainer.getItems()));
        newList.add(itemGoalData);
        this.itemGoalsInitDataContainer.setItems(newList.toArray(ItemGoalData[]::new));
    }

    public void addItemGoal(ItemGoal itemGoal) {
        this.itemGoalsInGameContainer.getItems().add(itemGoal);
    }

    public void save() {
        this.itemGoalsInGameContainer.save(ITEM_GOALS_IN_GAME_CONTAINER_PATH);
        this.itemGoalsInitDataContainer.save(ITEM_GOALS_INIT_DATA_CONTAINER_PATH);
    }
}
