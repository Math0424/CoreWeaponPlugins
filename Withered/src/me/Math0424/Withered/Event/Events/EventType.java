package me.Math0424.Withered.Event.Events;

import me.Math0424.Withered.Event.Events.Global.*;
import me.Math0424.Withered.Event.Events.Local.AirDropEvent;
import me.Math0424.Withered.Event.Events.Local.AssassinationEvent;
import me.Math0424.Withered.Event.Events.Local.DefendEvent;
import me.Math0424.Withered.Event.Events.Local.PointCaptureEvent;

import java.util.ArrayList;
import java.util.List;

public enum EventType {

    ENDGAMEDIAMOND(EndgameDiamondEvent.class, true),

    //Global events (entire server)
    MECHSUIT(MechSuitEvent.class, true),
    WEAPONSCACHE(WeaponsCacheEvent.class, true),
    DROPCRATE(DropCrateEvent.class, true),
    KILLCOUNTER(KillCounterEvent.class, true), //most kills at end of time wins, compass points to nearest player

    //Player events (not entire server)
    ASSASSINATION(AssassinationEvent.class, false), //kill a random person for money w/ compass pointing to them
    POINTCAPTURE(PointCaptureEvent.class, false), //random people get as close as possible to a random point at the end of a timer
    DEFEND(DefendEvent.class, false), //a person defends there location from other players trying to capture it (unlimited try's)
    AIRDROP(AirDropEvent.class, false); //a dumbed down weaponscache that will put players at a mad dash for the location first come first serve

    private final Class<? extends EventAbstract> clazz;
    private final boolean global;

    EventType(Class<? extends EventAbstract> clazz, boolean global) {
        this.clazz = clazz;
        this.global = global;
    }

    public EventAbstract getEventClass() {
        try {
            return this.getClazz().newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isGlobal() {
        return global;
    }

    public Class<? extends EventAbstract> getClazz() {
        return clazz;
    }

    public static List<EventType> getGlobalEvents() {
        List<EventType> events = new ArrayList<>();
        for (EventType m : EventType.values()) {
            if (m.isGlobal()) {
                if (m != EventType.ENDGAMEDIAMOND) {
                    events.add(m);
                }
            }
        }
        return events;
    }

    public static List<EventType> getLocalEvents() {
        List<EventType> events = new ArrayList<>();
        for (EventType m : EventType.values()) {
            if (!m.isGlobal()) {
                events.add(m);
            }
        }
        return events;
    }

}
