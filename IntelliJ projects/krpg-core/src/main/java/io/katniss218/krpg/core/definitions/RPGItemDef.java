package io.katniss218.krpg.core.definitions;

import io.katniss218.krpg.core.*;
import io.katniss218.krpg.core.items.RPGItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RPGItemDef
{
    public final int UNSELLABLE_VALUE = -1;

    public final String id; // required
    public Material baseItem; // required
    public String nbt;
    public int modelData;

    public int level; // required

    public RPGItemType type; // required
    public RPGRarityDef rarity;
    public String displayName; // required
    public List<String> displayDescription;

    public HashMap<PhysicalDamageType, ModifierSet> physicalDamage;
    public HashMap<MagicalDamageType, ModifierSet> magicalDamage;
    public ModifierSet attackSpeed;
    public ModifierSet critChance;
    public HashMap<PhysicalDamageType, ModifierSet> physicalArmor;
    public HashMap<MagicalDamageType, ModifierSet> magicalArmor;
    public ModifierSet maxHealth;
    public ModifierSet movementSpeed;
    public Double food;
    public Double potionEffects;

    public Integer durability;
    /**
     * Describes how much the item is worth when it is bought (not sold!)
     */
    public int value;

    public RPGItemDef( String id, Material baseItem, int level, RPGItemType type, String displayName )
    {
        this.id = id;
        this.baseItem = baseItem;
        this.level = level;
        this.type = type;
        this.displayName = displayName;
    }

    @Nullable
    public static RPGItemDef fromConfig( @Nonnull ConfigurationSection config )
    {
        String id = null;
        Material baseItem;
        int level;
        RPGItemType type;
        String displayName;
        try
        {
            id = config.getString( "id" );
            baseItem = Material.matchMaterial( config.getString( "base" ) ); // todo - this very much sucks, we want to use minecraft's IDs.
            level = config.getInt( "level" );
            type = RPGItemType.valueOf( config.getString( "level" ) );
            displayName = config.getString( "display.name" );
        }
        catch( Exception ex )
        {
            Bukkit.getLogger().warning("Loading item definition failed. id: " + id);
            return null;
        }
        RPGItemDef def = new RPGItemDef( id, baseItem, level, type, displayName );

        try
        {
            if( !config.contains( "rarity" ))
                def.rarity = RPGRarityRegistry.get( 0 );
            else
                def.rarity = RPGRarityRegistry.get( config.getInt( "rarity" ) );
        }
        catch( Exception ex )
        {
            return null;
        }

        try
        {
            def.displayDescription = config.getStringList( "display.description" );
        }
        catch( Exception ex )
        {
            def.displayDescription = new ArrayList<>();
        }

        // TODO - load the rest.

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