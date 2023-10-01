package io.katniss218.krpg.core.definitions;

import io.katniss218.krpg.core.*;
import io.katniss218.krpg.core.items.RPGItemType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RPGItemDef
{
    public static final int UNSELLABLE_VALUE = -1;

    @Nonnull
    public final String id; // required
    @Nonnull
    public Material baseItem = Material.STONE; // required
    @Nullable
    public String nbt = null;
    @Nullable
    public Integer modelData = null;

    @Range( from = 1, to = Integer.MAX_VALUE )
    public int level = 1;

    @Nonnull
    public RPGItemType type = RPGItemType.UNDEFINED; // required
    public int rarity = 0;
    @Nonnull
    public String displayName = "name_not_assigned"; // required
    @Nonnull
    public List<String> displayDescription = new ArrayList<>();

    // the player will apply every type of damage that they have specified as 'primary' on any of the equipped items.
    @Nonnull
    public HashMap<PhysicalDamageType, ModifierSet> physicalDamage = new HashMap<>();
    @Nonnull
    public HashMap<MagicalDamageType, ModifierSet> magicalDamage = new HashMap<>();
    @Nullable
    public ModifierSet attackSpeed = null;
    @Nullable
    public ModifierSet critChance = null;
    @Nonnull
    public HashMap<PhysicalDamageType, ModifierSet> physicalArmor = new HashMap<>();
    @Nonnull
    public HashMap<MagicalDamageType, ModifierSet> magicalArmor = new HashMap<>();
    @Nullable
    public ModifierSet maxHealth = null;
    @Nullable
    public ModifierSet movementSpeed = null;

    @Nullable
    public Double food = null;
    @Nullable
    public List<PotionEffectApplication> potionEffects = null;

    @Nullable
    public Integer durability = null;
    /**
     * Describes how much the item is worth when it is bought (not sold!)
     */
    public double value;

    public RPGItemDef( @Nonnull String id, @Nonnull Material baseItem, @Nonnull RPGItemType type, @Nonnull String displayName )
    {
        this.id = id;
        this.baseItem = baseItem;
        this.type = type;
        this.displayName = displayName;
    }

    @Nullable
    public static RPGItemDef fromConfig( @Nonnull ConfigurationSection config )
    {
        String id = null;
        Material baseItem;
        RPGItemType type;
        String displayName;
        try
        {
            id = config.getName();
            baseItem = Material.matchMaterial( config.getString( "base" ) ); // todo - this very much sucks, we want to use minecraft's IDs.
            type = RPGItemType.valueOf( config.getString( "type" ) );
            displayName = config.getString( "display.name" );
        }
        catch( Exception ex )
        {
            KRPGCore.getPluginLogger().warning( "Loading item definition failed. id: " + id );
            KRPGCore.getPluginLogger().info( ex.getMessage() );
            ex.printStackTrace( System.out );
            return null;
        }
        if( baseItem == null || displayName == null )
        {
            return null;
        }

        RPGItemDef def = new RPGItemDef( id, baseItem, type, displayName );

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

        if( config.contains( "rarity" ) )
        {
            try
            {
                def.rarity = config.getInt( "rarity" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "display.description" ) )
        {
            try
            {
                def.displayDescription = config.getStringList( "display.description" );
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

        if( config.contains( "model_data" ) )
        {
            try
            {
                def.modelData = config.getInt( "model_data" );
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
                    ModifierSet ms = ModifierSet.getModifierSet( configSection, key );
                    if( ms != null )
                    {
                        def.physicalDamage.put( damageType, ms );
                    }
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
                    ModifierSet ms = ModifierSet.getModifierSet( configSection, key );
                    if( ms != null )
                    {
                        def.magicalDamage.put( damageType, ms );
                    }
                }
                catch( Exception ex )
                {
                }
            }
        }

        if( config.contains( "attack_speed" ) )
        {
            try
            {
                def.attackSpeed = ModifierSet.getModifierSet( config, "attack_speed" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "crit_chance" ) )
        {
            try
            {
                def.critChance = ModifierSet.getModifierSet( config, "crit_chance" );
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
                    ModifierSet ms = ModifierSet.getModifierSet( configSection, key );
                    if( ms != null )
                    {
                        def.physicalArmor.put( damageType, ms );
                    }
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
                    ModifierSet ms = ModifierSet.getModifierSet( configSection, key );
                    if( ms != null )
                    {
                        def.magicalArmor.put( damageType, ms );
                    }
                }
                catch( Exception ex )
                {
                }
            }
        }

        if( config.contains( "max_health" ) )
        {
            try
            {
                def.maxHealth = ModifierSet.getModifierSet( config, "max_health" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "movement_speed" ) )
        {
            try
            {
                def.movementSpeed = ModifierSet.getModifierSet( config, "movement_speed" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "food" ) )
        {
            try
            {
                def.food = config.getDouble( "food" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "potion_effects" ) )
        {
            try
            {
                var potioneffects = config.getStringList( "potion_effects" );
                for( final var eff : potioneffects )
                {
                    def.potionEffects.add( PotionEffectApplication.valueOf( eff ) );
                }
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "durability" ) )
        {
            try
            {
                def.durability = config.getInt( "durability" );
            }
            catch( Exception ex )
            {
            }
        }

        if( config.contains( "value" ) )
        {
            try
            {
                def.value = config.getDouble( "value" );
            }
            catch( Exception ex )
            {
            }
        }

        return def;
    }

    @Nonnull
    public ConfigurationSection toConfig()
    {
        ConfigurationSection config = new MemoryConfiguration();

        // TODO - do stuff.

        return config;
    }
}