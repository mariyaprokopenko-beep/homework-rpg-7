package com.narxoz.rpg.boss;

import com.narxoz.rpg.strategy.CombatStrategy;
import com.narxoz.rpg.strategy.AggressiveStrategy;
import com.narxoz.rpg.strategy.BalancedStrategy;
import com.narxoz.rpg.observer.EventPublisher;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;

public class DungeonBoss implements GameObserver {
    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private CombatStrategy strategy;
    private int currentPhase;
    private final EventPublisher publisher;

    public DungeonBoss(String name, int hp, int attackPower, int defense, EventPublisher publisher) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.publisher = publisher;
        this.currentPhase = 1;
        this.strategy = new BalancedStrategy();
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public int getDefense() { return defense; }
    public CombatStrategy getStrategy() { return strategy; }
    public boolean isAlive() { return hp > 0; }

    public void takeDamage(int amount) {
        int oldHp = hp;
        hp = Math.max(0, hp - amount);
        int damageDealt = oldHp - hp;

        publisher.fireEvent(new GameEvent(GameEventType.ATTACK_LANDED, name, damageDealt));

        double hpPercent = (double)hp / maxHp;
        int newPhase = currentPhase;

        if (hpPercent < 0.3 && currentPhase < 3) {
            newPhase = 3;
        } else if (hpPercent < 0.6 && currentPhase < 2) {
            newPhase = 2;
        }

        if (newPhase != currentPhase) {
            currentPhase = newPhase;
            publisher.fireEvent(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, currentPhase));
        }

        if (hp <= 0) {
            publisher.fireEvent(new GameEvent(GameEventType.BOSS_DEFEATED, name, 0));
        }
    }

    public int getEffectiveDamage() {
        return strategy.calculateDamage(attackPower);
    }

    public int getEffectiveDefense() {
        return strategy.calculateDefense(defense);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED && event.getSourceName().equals(name)) {
            switchStrategyForPhase(event.getValue());
        }
    }

    private void switchStrategyForPhase(int phase) {
        switch (phase) {
            case 1:
                strategy = new BalancedStrategy();
                System.out.println("[BOSS] Phase 1: Balanced strategy activated");
                break;
            case 2:
                strategy = new AggressiveStrategy();
                System.out.println("[BOSS] Phase 2: Aggressive strategy activated!");
                break;
            case 3:
                strategy = new AggressiveStrategy();
                System.out.println("[BOSS] Phase 3: DESPERATE AGGRESSION activated!");
                break;
        }
    }
}