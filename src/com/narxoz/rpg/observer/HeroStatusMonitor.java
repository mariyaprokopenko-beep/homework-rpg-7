package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public class HeroStatusMonitor implements GameObserver {
    private final List<Hero> heroes;

    public HeroStatusMonitor(List<Hero> heroes) {
        this.heroes = heroes;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.HERO_LOW_HP || event.getType() == GameEventType.HERO_DIED) {
            printStatus();
        }
    }

    private void printStatus() {
        System.out.println("[STATUS] Hero Status:");
        for (Hero hero : heroes) {
            String status = hero.isAlive() ? "HP: " + hero.getHp() + "/" + hero.getMaxHp() : "DEAD";
            System.out.println("  - " + hero.getName() + ": " + status);
        }
    }
}