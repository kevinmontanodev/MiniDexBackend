package org.kmontano.minidex.domain.battle.engine;

import org.kmontano.minidex.domain.battle.action.BattleAction;
import org.kmontano.minidex.domain.battle.model.BattleSide;
import org.kmontano.minidex.domain.battle.action.SwitchAction;
import org.kmontano.minidex.domain.battle.model.BattleContext;

import org.kmontano.minidex.domain.battle.event.BattleEventDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * Core engine responsible for executing battle turns.
 *
 * <p>This class coordinates the execution of actions from both the player
 * and the enemy. It determines the execution order based on speed and
 * resolves the resulting battle events.</p>
 *
 * Responsibilities:
 * - Determine action order
 * - Execute actions
 * - Collect battle events
 * - Update the battle context
 *
 * The engine does not decide actions; it only executes them.
 * Enemy decisions are delegated to EnemyAiDecisionService.
 */
@Component
public class BattleEngine {
    public void executeTurn(BattleContext context, BattleAction playerAction, BattleAction enemyAction, BattleEventCollector collector){
        List<BattleAction> orderedActions = orderActions(playerAction, enemyAction, context);

        for (BattleAction action: orderedActions){
            if (context.getPlayer().isFainted() && !(action instanceof SwitchAction && ((SwitchAction) action).getSide() == BattleSide.PLAYER)){
                continue;
            };

            if (context.getEnemy().isFainted() && !(action instanceof SwitchAction && ((SwitchAction) action).getSide() == BattleSide.ENEMY)) {
                continue;
            }

            BattleEventDTO event = action.execute(context);
            collector.add(event);
        }
    }

    public List<BattleAction> orderActions(BattleAction playerAction, BattleAction enemyAction, BattleContext context){
        return Stream.of(playerAction, enemyAction)
                .sorted((a,b) -> {
                    int priorityCompare = Integer.compare(b.getPriority(), a.getPriority());

                    if (priorityCompare != 0) return priorityCompare;

                    int speedA = a.getActor(context).getSpeed();
                    int speedB = b.getActor(context).getSpeed();

                    int speedCompare = Integer.compare(speedB, speedA);

                    if (speedCompare != 0) return speedCompare;

                    return ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
                })
                .toList();
    }
}
