package io.katniss218.krpg.core.combat;

import io.katniss218.krpg.core.PhysicalDamageType;
import io.katniss218.krpg.core.Stats;

import java.util.Random;

public class DamageCalculator
{
    static Random rand = new Random();

    public static double getDamageTaken( Stats attacker, Stats victim )
    {
        // sum up all the damage types.
        double totalDamage = 0.0;

        double critChance = 0;
        if( attacker.critChance != null )
        {
            critChance = attacker.critChance;
        }

        for( var dmgType : attacker.physicalDamage.keySet() )
        {
            double damage = attacker.physicalDamage.get( dmgType );
            double armor = victim.physicalArmor.getOrDefault( dmgType, 0.0 );
            totalDamage += damageFormoola( damage, critChance, armor );
        }
        for( var dmgType : attacker.magicalDamage.keySet() )
        {
            double damage = attacker.magicalDamage.get( dmgType );
            double armor = victim.magicalArmor.getOrDefault( dmgType, 0.0 );
            totalDamage += damageFormoola( damage, critChance, armor );
        }
        return totalDamage;
    }

    public static double damageFormoola( double avgDamage, double critChance, double armor )
    {
        double damageMultiplier = ((double)rand.nextInt( 75, 126 )) / 100.0;
        double damage = avgDamage * damageMultiplier;

        damage -= armor * 0.5;

        if( damage < 1 )
        {
            damage = 1;
        }
        boolean isCrit = rand.nextInt(0, 100 ) < (critChance * 100);
        if( isCrit )
        {
            damage *= 2;
        }
        return damage;
    }
}
