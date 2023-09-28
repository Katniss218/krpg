package io.katniss218.krpg.core.definitions;

import io.katniss218.krpg.core.MagicalDamageType;
import io.katniss218.krpg.core.PhysicalDamageType;
import io.katniss218.krpg.core.entities.RPGEntityType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class RPGEntityDef
{
    @Nonnull
    public final String id;
    @Nonnull
    public EntityType baseEntity = EntityType.ZOMBIE;

    @Range( from = 1, to = Integer.MAX_VALUE )
    public int level = 1;
    @Nonnull
    public RPGEntityType entityType = RPGEntityType.NORMAL;

    public double maxHealth;
    public HashMap<PhysicalDamageType, Double> physicalDamage;
    public HashMap<MagicalDamageType, Double> magicalDamage;
    public double critChance;
    public HashMap<PhysicalDamageType, Double> physicalArmor;
    public HashMap<MagicalDamageType, Double> magicalArmor;
    public double aggroRange = 16.0;
    public double movementSpeed = 0.2;
    public double knockbackResistance = 0.0;

    @Nullable
    public String lootTable;

    public RPGEntityDef( @Nonnull String id, EntityType baseEntity, double maxHealth )
    {
        this.id = id;
        this.baseEntity = baseEntity;
        this.maxHealth = maxHealth;
    }
    @Nullable
    public static RPGEntityDef fromConfig( @Nonnull ConfigurationSection config )
    {
        String id;
        EntityType baseEntity;
        double maxHealth;
        try
        {
            id = config.getName();
            baseEntity = EntityType.fromName( config.getString( "base" ) );
        }

        RPGEntityDef def = new RPGEntityDef( id, baseEntity, maxHealth );

        return def;
    }
}
