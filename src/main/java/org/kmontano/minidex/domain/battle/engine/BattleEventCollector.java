package org.kmontano.minidex.domain.battle.engine;

import org.kmontano.minidex.domain.battle.event.BattleEventDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects battle events generated during a turn.
 *
 * <p>Actions executed by the battle engine produce events
 * such as ATTACK, FAINT, FINISH_BATTLE, STRATEGIC_SWITCH or SWITCH. This collector stores
 * them in the correct order so they can be sent to the frontend.</p>
 *
 * The frontend processes these events sequentially to trigger
 * battle animations and UI updates.
 */
@Component
public class BattleEventCollector {
    private final List<BattleEventDTO> events = new ArrayList<>();

    public void add(BattleEventDTO event){
        if (event != null){
            events.add(event);
        }
    }

    public void addAll(List<BattleEventDTO> newEvents){
        if (newEvents != null){
            events.addAll(newEvents);
        }
    }

    public List<BattleEventDTO> consume(){
        List<BattleEventDTO> copy = new ArrayList<>(events);
        events.clear();
        return copy;
    }

    public boolean isEmpty(){
        return events.isEmpty();
    }
}
