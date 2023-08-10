package fr.teamunc.uncsurvivals2.helpers;

import fr.teamunc.base_unclib.BaseLib;
import fr.teamunc.base_unclib.controllers.UNCEventController;
import fr.teamunc.uncsurvivals2.metier.models.UNCRandomPotionEffectEvent;

public class EventsInitiator implements IInitialisator{
    @Override
    public void init() {
        BaseLib.getUNCEventController().registerEvent(new UNCRandomPotionEffectEvent());
    }
}
