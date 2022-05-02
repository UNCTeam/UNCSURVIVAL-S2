package fr.teamunc.uncsurvivals2.minecraft;

public class Initialisator {


    // SINGLETON
    private static Initialisator instance;
    private Initialisator(){
        super();
    }
    public static Initialisator Get() {
        if (instance == null) instance = new Initialisator();
        return instance;
    }
    // END SINGLETON

    public void initCmdExecAndEvntListeners() {
        this.initCommandsExec();
        this.initEventsListeners();
    }

    /**
     * Ajoute les commandes au server
     */
    private void initCommandsExec() {

    }

    /**
     * Ajoute les events listeners au server
     */
    private void initEventsListeners() {

    }
}
