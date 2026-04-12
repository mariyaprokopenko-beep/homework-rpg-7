package com.narxoz.rpg.observer;

import java.util.Random;

public class LootDropper implements GameObserver {
    private final Random random;

    public LootDropper(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case BOSS_PHASE_CHANGED:
                int phase = event.getValue();
                if (phase == 2) {
                    System.out.println("[LOOT] Phase 2 drop: Mysterious Gem!");
                } else if (phase == 3) {
                    System.out.println("[LOOT] Phase 3 drop: Ancient Rune!");
                }
                break;
            case BOSS_DEFEATED:
                String[] epicLoot = {"Legendary Sword", "Dragon Scale Armor", "Phoenix Feather", "Cursed Crown"};
                String loot = epicLoot[random.nextInt(epicLoot.length)];
                System.out.println("[LOOT] Boss defeated! Epic drop: " + loot + "!");
                break;
        }
    }
}