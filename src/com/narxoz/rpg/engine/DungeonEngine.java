package com.narxoz.rpg.engine;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.boss.DungeonBoss;
import com.narxoz.rpg.observer.EventPublisher;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import java.util.List;

public class DungeonEngine {
    private final List<Hero> heroes;
    private final DungeonBoss boss;
    private final EventPublisher publisher;
    private final int maxRounds;

    public DungeonEngine(List<Hero> heroes, DungeonBoss boss, EventPublisher publisher, int maxRounds) {
        this.heroes = heroes;
        this.boss = boss;
        this.publisher = publisher;
        this.maxRounds = maxRounds;
    }

    public EncounterResult runEncounter() {
        int rounds = 0;
        boolean lowHpFired = false;

        while (rounds < maxRounds && boss.isAlive() && heroes.stream().anyMatch(Hero::isAlive)) {
            rounds++;
            System.out.println("\n=== ROUND " + rounds + " ===");

            // Heroes attack boss
            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;

                int damage = hero.getEffectiveDamage();
                int defense = boss.getEffectiveDefense();
                int finalDamage = Math.max(1, damage - defense);

                boss.takeDamage(finalDamage);
                System.out.println(hero.getName() + " hits boss for " + finalDamage + " damage!");

                if (!boss.isAlive()) break;
            }

            if (!boss.isAlive()) break;

            // Boss attacks heroes
            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;

                int damage = boss.getEffectiveDamage();
                int defense = hero.getEffectiveDefense();
                int finalDamage = Math.max(1, damage - defense);

                hero.takeDamage(finalDamage);
                publisher.fireEvent(new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), finalDamage));
                System.out.println(boss.getName() + " hits " + hero.getName() + " for " + finalDamage + " damage!");

                if (hero.getHp() > 0 && hero.getHp() <= hero.getMaxHp() * 0.3 && !lowHpFired) {
                    publisher.fireEvent(new GameEvent(GameEventType.HERO_LOW_HP, hero.getName(), hero.getHp()));
                    lowHpFired = true;
                }

                if (!hero.isAlive()) {
                    publisher.fireEvent(new GameEvent(GameEventType.HERO_DIED, hero.getName(), 0));
                }
            }

            System.out.print("Status - Boss HP: " + boss.getHp() + " | Heroes: ");
            for (Hero h : heroes) {
                System.out.print(h.getName() + "(" + h.getHp() + ") ");
            }
            System.out.println();
            lowHpFired = false;
        }

        int survivingHeroes = (int) heroes.stream().filter(Hero::isAlive).count();
        boolean heroesWon = boss.getHp() <= 0;

        if (heroesWon) {
            System.out.println("\n*** VICTORY! The boss has been defeated! ***");
        } else if (survivingHeroes == 0) {
            System.out.println("\n*** DEFEAT! All heroes have fallen... ***");
        } else {
            System.out.println("\n*** DRAW! Max rounds reached. ***");
        }

        return new EncounterResult(heroesWon, rounds, survivingHeroes);
    }
}