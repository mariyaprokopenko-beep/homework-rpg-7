package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.boss.DungeonBoss;
import com.narxoz.rpg.engine.DungeonEngine;
import com.narxoz.rpg.engine.EncounterResult;
import com.narxoz.rpg.strategy.AggressiveStrategy;
import com.narxoz.rpg.strategy.DefensiveStrategy;
import com.narxoz.rpg.strategy.BalancedStrategy;
import com.narxoz.rpg.observer.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for Homework 7 — The Cursed Dungeon: Boss Encounter System.
 *
 * Build your heroes, boss, observers, and engine here, then run the encounter.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Homework 7: The Cursed Dungeon ===\n");

        // Create event publisher
        EventPublisher publisher = new EventPublisher();

        // Create at least 3 heroes with different combat strategies
        Hero warrior = new Hero("Grom", 180, 28, 15, new AggressiveStrategy());
        Hero paladin = new Hero("Uther", 200, 20, 25, new DefensiveStrategy());
        Hero rogue = new Hero("Valeera", 140, 30, 10, new BalancedStrategy());
        List<Hero> heroes = List.of(warrior, paladin, rogue);

        // Create a DungeonBoss with meaningful stats (high enough HP to see all phases)
        DungeonBoss boss = new DungeonBoss("The Cursed Lich King", 400, 25, 12, publisher);

        // Register boss as observer (so it can receive BOSS_PHASE_CHANGED events)
        publisher.registerObserver(boss);

        // Instantiate and register all 5 observers
        BattleLogger logger = new BattleLogger();
        AchievementTracker achievements = new AchievementTracker();
        PartySupport partySupport = new PartySupport(heroes, 25, 42L);
        HeroStatusMonitor statusMonitor = new HeroStatusMonitor(heroes);
        LootDropper loot = new LootDropper(99L);

        publisher.registerObserver(logger);
        publisher.registerObserver(achievements);
        publisher.registerObserver(partySupport);
        publisher.registerObserver(statusMonitor);
        publisher.registerObserver(loot);

        // Print initial strategies
        System.out.println("--- Initial Strategies ---");
        System.out.println(warrior.getName() + ": " + warrior.getStrategy().getName());
        System.out.println(paladin.getName() + ": " + paladin.getStrategy().getName());
        System.out.println(rogue.getName() + ": " + rogue.getStrategy().getName());
        System.out.println(boss.getName() + ": " + boss.getStrategy().getName());

        // Switch at least one hero's strategy during the battle
        System.out.println("\n--- Strategy Switch Demo ---");
        System.out.println(rogue.getName() + " switches to Aggressive strategy!");
        rogue.setStrategy(new AggressiveStrategy());

        // Create a DungeonEngine and run the encounter
        DungeonEngine engine = new DungeonEngine(heroes, boss, publisher, 30);
        EncounterResult result = engine.runEncounter();

        // Print the EncounterResult at the end
        System.out.println("\n=== ENCOUNTER RESULT ===");
        System.out.println("Heroes won: " + result.isHeroesWon());
        System.out.println("Rounds played: " + result.getRoundsPlayed());
        System.out.println("Surviving heroes: " + result.getSurvivingHeroes());

        System.out.println("\n=== Demo Complete ===");
    }
}