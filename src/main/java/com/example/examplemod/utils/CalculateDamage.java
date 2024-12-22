package com.example.examplemod.utils;

public class CalculateDamage {
    public static double DamageCalculationMelee(int Damage, int Strength, int CritDamage){
        int initialDmg = (5+Damage)*(1+Strength/100);
        int CombatLevel = 10;
        double DamageMultiplier =(1+(CombatLevel*0.01));
        double damage = initialDmg*DamageMultiplier*(1+CritDamage/100);
        System.out.println(damage/10);
        return damage/1;
    }
}