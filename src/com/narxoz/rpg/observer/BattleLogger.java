package com.narxoz.rpg.observer;

public class BattleLogger implements GameObserver {
    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED:
                System.out.println("[LOG] " + event.getSourceName() + " dealt " + event.getValue() + " damage!");
                break;
            case HERO_LOW_HP:
                System.out.println("[LOG] " + event.getSourceName() + " is critically low! HP: " + event.getValue());
                break;
            case HERO_DIED:
                System.out.println("[LOG] " + event.getSourceName() + " has fallen in battle!");
                break;
            case BOSS_PHASE_CHANGED:
                System.out.println("[LOG] Boss entered PHASE " + event.getValue() + "!");
                break;
            case BOSS_DEFEATED:
                System.out.println("[LOG] Boss has been defeated!");
                break;
        }
    }
}