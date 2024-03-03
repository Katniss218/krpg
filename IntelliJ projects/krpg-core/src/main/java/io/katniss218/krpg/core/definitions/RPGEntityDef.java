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
    public EntityType baseEntity;

    @Range( from = 1, to = Integer.MAX_VALUE )
    public int level = 1;
    @Nonnull
    public RPGEntityType entityType = RPGEntityType.NORMAL;

    @Nonnull
    public String displayName;
    @Nullable
    public String nbt;

    // equipment
    @Nullable
    public RPGEntityEquipment mainhand;
    @Nullable
    public RPGEntityEquipment offhand;
    @Nullable
    public RPGEntityEquipment head;
    @Nullable
    public RPGEntityEquipment chest;
    @Nullable
    public RPGEntityEquipment legs;
    @Nullable
    public RPGEntityEquipment feet;

    public double maxHealth;
    @Nonnull
    public HashMap<PhysicalDamageType, Double> physicalDamage = new HashMap<>();
    @Nonnull
    public HashMap<MagicalDamageType, Double> magicalDamage = new HashMap<>();
    public double critChance = 0.0;
    @Nonnull
    public HashMap<PhysicalDamageType, Double> physicalArmor = new HashMap<>();
    @Nonnull
    public HashMap<MagicalDamageType, Double> magicalArmor = new HashMap<>();
    public double aggroRange = 16.0;
    public double movementSpeed = 0.2;
    public double knockbackResistance = 0.0;

    @Nullable
    public String lootTableId;

    public RPGEntityDef( @Nonnull String id, @Nonnull EntityType baseEntity, @Nonnull String displayName, double maxHealth )
    {
        this.id = id;
        this.baseEntity = baseEntity;
        this.maxHealth = maxHealth;
        this.displayName = displayName;
    }

    @Nullable
    public static RPGEntityDef fromConfig( @Nonnull ConfigurationSection config )
    {
        String id;
        EntityType baseEntity;
        double maxHealth;
        String displayName;
        try
        {
            id = config.getName();
            baseEntity = EntityType.fromName( config.getString( "base" ) ); // no good way to map these. maybe create a custom map?
            maxHealth = config.getDouble( "max_health" );
            displayName = config.getString( "display.name" );
        }
        catch( Exception ex )
        {
            return null;
        }
        if( baseEntity == null || displayName == null || maxHealth <= 0 )
        {
            return null;
        }

        RPGEntityDef def = new RPGEntityDef( id, baseEntity, displayName, maxHealth );

        if( config.contains( "level" ) )
        {
            try
            {
                def.level = config.getInt( "level" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "type" ) )
        {
            try
            {
                def.entityType = RPGEntityType.valueOf( config.getString( "type" ) );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "nbt" ) )
        {
            try
            {
                def.nbt = config.getString( "nbt" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "display.mainhand" ) )
        {
            try
            {
                var eqSection = config.getConfigurationSection( "display.mainhand" );
                Material mat = Material.matchMaterial( eqSection.getString( "base" ) );
                String nbt = eqSection.getString( "nbt" );
                def.mainhand = new RPGEntityEquipment( mat, nbt );
            }
            catch( Exception ex )
            {
            }
        }
        if( config.contains( "display.offhand" ) )
        {
            try
            {
                var eqSection = config.getConfigurationSection( "display.offhand" );
                Material mat = Material.matchMaterial( eqSection.getString( "base" ) );
                String nbt = eqSection.getString( "nbt" );
                def.offhand = new RPGEntityEquipment( mat, nbt );
            }
            catch( Exception ex )
            {
            }
        }
        if( config.contains( "display.head" ) )
        {
            try
            {
                var eqSection = config.getConfigurationSection( "display.head" );
                Material mat = Material.matchMaterial( eqSection.getString( "base" ) );
                String nbt = eqSection.getString( "nbt" );
                def.head = new RPGEntityEquipment( mat, nbt );
            }
            catch( Exception ex )
            {
            }
        }
        if( config.contains( "display.chest" ) )
        {
            try
            {
                var eqSection = config.getConfigurationSection( "display.chest" );
                Material mat = Material.matchMaterial( eqSection.getString( "base" ) );
                String nbt = eqSection.getString( "nbt" );
                def.chest = new RPGEntityEquipment( mat, nbt );
            }
            catch( Exception ex )
            {
            }
        }
        if( config.contains( "display.legs" ) )
        {
            try
            {
                var eqSection = config.getConfigurationSection( "display.legs" );
                Material mat = Material.matchMaterial( eqSection.getString( "base" ) );
                String nbt = eqSection.getString( "nbt" );
                def.legs = new RPGEntityEquipment( mat, nbt );
            }
            catch( Exception ex )
            {
            }
        }
        if( config.contains( "display.feet" ) )
        {
            try
            {
                var eqSection = config.getConfigurationSection( "display.feet" );
                Material mat = Material.matchMaterial( eqSection.getString( "base" ) );
                String nbt = eqSection.getString( "nbt" );
                def.feet = new RPGEntityEquipment( mat, nbt );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "physical_damage" ) )
        {
            var configSection = config.getConfigurationSection( "physical_damage" );
            for( final var key : configSection.getKeys( false ) )
            {
                try
                {
                    PhysicalDamageType damageType = PhysicalDamageType.valueOf( key );
                    double damage = configSection.getDouble( key );
                    def.physicalDamage.put( damageType, damage );
                }
                catch( Exception ex )
                {
                }
            }
        }

        if( config.contains( "magical_damage" ) )
        {
            var configSection = config.getConfigurationSection( "magical_damage" );
            for( final var key : configSection.getKeys( false ) )
            {
                try
                {
                    MagicalDamageType damageType = MagicalDamageType.valueOf( key );
                    double damage = configSection.getDouble( key );
                    def.magicalDamage.put( damageType, damage );
                }
                catch( Exception ex )
                {
                }
            }
        }

        if( config.contains( "crit_chance" ) )
        {
            try
            {
                def.critChance = config.getDouble( "crit_chance" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "physical_armor" ) )
        {
            var configSection = config.getConfigurationSection( "physical_armor" );
            for( final var key : configSection.getKeys( false ) )
            {
                try
                {
                    PhysicalDamageType damageType = PhysicalDamageType.valueOf( key );
                    double armor = configSection.getDouble( key );
                    def.physicalArmor.put( damageType, armor );
                }
                catch( Exception ex )
                {
                }
            }
        }

        if( config.contains( "magical_armor" ) )
        {
            var configSection = config.getConfigurationSection( "magical_armor" );
            for( final var key : configSection.getKeys( false ) )
            {
                try
                {
                    MagicalDamageType damageType = MagicalDamageType.valueOf( key );
                    double armor = configSection.getDouble( key );
                    def.magicalArmor.put( damageType, armor );
                }
                catch( Exception ex )
                {
                }
            }
        }

        if( config.contains( "aggro_range" ) )
        {
            try
            {
                def.aggroRange = config.getDouble( "aggro_range" );
            }
            catch( Exception ex )
            {
            }
        }
        if( config.contains( "movement_speed" ) )
        {
            try
            {
                def.movementSpeed = config.getDouble( "movement_speed" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "knockback_resistance" ) )
        {
            try
            {
                def.knockbackResistance = config.getDouble( "knockback_resistance" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "loot_table" ) )
        {
            try
            {
                def.lootTableId = config.getString( "loot_table" );
            }
            catch( Exception ex )
            {
            }
        }

        return def;
    }
}
