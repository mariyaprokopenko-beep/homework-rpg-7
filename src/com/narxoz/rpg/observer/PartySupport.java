package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;
import java.util.Random;

public class PartySupport implements GameObserver {
    private final List<Hero> heroes;
    private final int healAmount;
    private final Random random;

    public PartySupport(List<Hero> heroes, int healAmount, long seed) {
        this.heroes = heroes;
        this.healAmount = healAmount;
        this.random = new Random(seed);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.HERO_LOW_HP) {
            List<Hero> aliveHeroes = heroes.stream().filter(Hero::isAlive).toList();
            if (!aliveHeroes.isEmpty()) {
                Hero target = aliveHeroes.get(random.nextInt(aliveHeroes.size()));
                target.heal(healAmount);
                System.out.println("[SUPPORT] " + target.getName() + " was healed for " + healAmount + " HP!");
            }
        }
    }
}