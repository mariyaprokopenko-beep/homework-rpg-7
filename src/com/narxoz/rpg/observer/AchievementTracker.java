package com.narxoz.rpg.observer;

public class AchievementTracker implements GameObserver {
    private boolean firstBlood = false;
    private boolean bossSlayer = false;
    private boolean relentless = false;
    private int attackCount = 0;

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED:
                attackCount++;
                if (!firstBlood) {
                    firstBlood = true;
                    System.out.println("[ACHIEVEMENT] First Blood - First attack landed!");
                }
                if (!relentless && attackCount >= 10) {
                    relentless = true;
                    System.out.println("[ACHIEVEMENT] Relentless - 10 attacks landed!");
                }
                break;
            case BOSS_DEFEATED:
                if (!bossSlayer) {
                    bossSlayer = true;
                    System.out.println("[ACHIEVEMENT] Boss Slayer - Defeated the dungeon boss!");
                }
                break;
            case HERO_DIED:
                System.out.println("[ACHIEVEMENT] Fallen Hero - A hero has died in battle");
                break;
        }
    }
}