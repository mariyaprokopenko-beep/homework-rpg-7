package com.narxoz.rpg.strategy;

public class AggressiveStrategy implements CombatStrategy {
    @Override
    public int calculateDamage(int basePower) {
        return (int)(basePower * 1.5);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int)(baseDefense * 0.7);
    }

    @Override
    public String getName() {
        return "Aggressive (+50% damage, -30% defense)";
    }
}