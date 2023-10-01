package io.katniss218.krpg.core;

import io.katniss218.krpg.core.definitions.RPGEntityDef;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.entities.RPGEntityData;
import io.katniss218.krpg.core.items.EquipmentSlot;
import io.katniss218.krpg.core.items.RPGItemData;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class Stats
{
    // stats of an entity.
    // base for rpgentities
    // calculated from equipment for players.

    @Nullable
    public Double maxHealth;
    @Nonnull
    public HashMap<PhysicalDamageType, Double> physicalDamage = new HashMap<>();
    @Nonnull
    public HashMap<MagicalDamageType, Double> magicalDamage = new HashMap<>();
    @Nullable
    public Double critChance;
    @Nullable
    public Double attackSpeed;
    @Nonnull
    public HashMap<PhysicalDamageType, Double> physicalArmor = new HashMap<>();
    @Nonnull
    public HashMap<MagicalDamageType, Double> magicalArmor = new HashMap<>();
    @Nullable
    public Double aggroRange;
    @Nullable
    public Double movementSpeed;
    @Nullable
    public Double knockbackResistance;

    void multiplyBy( Stats other )
    {
        if( other.maxHealth != null )
        {
            this.maxHealth *= other.maxHealth;
        }
        for( var key : this.physicalDamage.keySet() )
        {
            Double statsValue = this.physicalDamage.get( key );

            statsValue *= other.physicalDamage.getOrDefault( key, 1.0 );

            this.physicalDamage.put( key, statsValue );
        }
        for( var key : this.magicalDamage.keySet() )
        {
            Double statsValue = this.magicalDamage.get( key );

            statsValue *= other.magicalDamage.getOrDefault( key, 1.0 );

            this.magicalDamage.put( key, statsValue );
        }
        if( other.attackSpeed != null )
        {
            this.attackSpeed *= other.attackSpeed;
        }
        if( other.critChance != null )
        {
            this.critChance *= other.critChance;
        }
        for( var key : this.physicalArmor.keySet() )
        {
            Double statsValue = this.physicalArmor.get( key );

            statsValue *= other.physicalArmor.getOrDefault( key, 1.0 );

            this.physicalArmor.put( key, statsValue );
        }
        for( var key : this.magicalArmor.keySet() )
        {
            Double statsValue = this.magicalArmor.get( key );

            statsValue *= other.magicalArmor.getOrDefault( key, 1.0 );

            this.magicalArmor.put( key, statsValue );
        }
        if( other.aggroRange != null )
        {
            this.aggroRange *= other.aggroRange;
        }
        if( other.movementSpeed != null )
        {
            this.movementSpeed *= other.movementSpeed;
        }
        if( other.knockbackResistance != null )
        {
            this.knockbackResistance *= other.knockbackResistance;
        }
    }

    static void AddToStatsPrimary( Stats stats, ItemStack item, EquipmentSlot slot )
    {
        RPGItemData itemData = RPGItemData.getFrom( item );
        if( itemData == null )
        {
            return;
        }

        var def = RPGItemRegistry.get( itemData.getID() );
        if( def == null )
        {
            return;
        }

        if( !def.type.IsValid( slot, true ) )
        {
            return;
        }

        for( var key : def.physicalDamage.keySet() )
        {
            Double statsValue = stats.physicalDamage.getOrDefault( key, 0.0 );

            statsValue += def.physicalDamage.get( key ).getPrimary();

            stats.physicalDamage.put( key, statsValue );
        }
        for( var key : def.magicalDamage.keySet() )
        {
            Double statsValue = stats.magicalDamage.getOrDefault( key, 0.0 );

            statsValue += def.magicalDamage.get( key ).getPrimary();

            stats.magicalDamage.put( key, statsValue );
        }
        if( def.attackSpeed != null )
        {
            stats.attackSpeed += def.attackSpeed.getPrimary();
        }
        if( def.critChance != null )
        {
            stats.critChance += def.critChance.getPrimary();
        }
        for( var key : def.physicalArmor.keySet() )
        {
            Double statsValue = stats.physicalArmor.getOrDefault( key, 0.0 );

            statsValue += def.physicalArmor.get( key ).getPrimary();

            stats.physicalArmor.put( key, statsValue );
        }
        for( var key : def.magicalArmor.keySet() )
        {
            Double statsValue = stats.magicalArmor.getOrDefault( key, 0.0 );

            statsValue += def.magicalArmor.get( key ).getPrimary();

            stats.magicalArmor.put( key, statsValue );
        }
        if( def.maxHealth != null )
        {
            stats.maxHealth += def.maxHealth.getPrimary();
        }
        if( def.movementSpeed != null )
        {
            stats.movementSpeed += def.movementSpeed.getPrimary();
        }
    }

    static void AddToStatsAdditionalFlat( Stats stats, ItemStack item, EquipmentSlot slot )
    {
        RPGItemData itemData = RPGItemData.getFrom( item );
        if( itemData == null )
        {
            return;
        }

        var def = RPGItemRegistry.get( itemData.getID() );
        if( def == null )
        {
            return;
        }

        if( !def.type.IsValid( slot, false ) )
        {
            return;
        }

        for( var key : def.physicalDamage.keySet() )
        {
            Double statsValue = stats.physicalDamage.getOrDefault( key, 0.0 );

            statsValue += def.physicalDamage.get( key ).getAdditionalFlat();

            stats.physicalDamage.put( key, statsValue );
        }
        for( var key : def.magicalDamage.keySet() )
        {
            Double statsValue = stats.magicalDamage.getOrDefault( key, 0.0 );

            statsValue += def.magicalDamage.get( key ).getAdditionalFlat();

            stats.magicalDamage.put( key, statsValue );
        }
        if( def.attackSpeed != null )
        {
            stats.attackSpeed += def.attackSpeed.getAdditionalFlat();
        }
        if( def.critChance != null )
        {
            stats.critChance += def.critChance.getAdditionalFlat();
        }
        for( var key : def.physicalArmor.keySet() )
        {
            Double statsValue = stats.physicalArmor.getOrDefault( key, 0.0 );

            statsValue += def.physicalArmor.get( key ).getAdditionalFlat();

            stats.physicalArmor.put( key, statsValue );
        }
        for( var key : def.magicalArmor.keySet() )
        {
            Double statsValue = stats.magicalArmor.getOrDefault( key, 0.0 );

            statsValue += def.magicalArmor.get( key ).getAdditionalFlat();

            stats.magicalArmor.put( key, statsValue );
        }
        if( def.maxHealth != null )
        {
            stats.maxHealth += def.maxHealth.getAdditionalFlat();
        }
        if( def.movementSpeed != null )
        {
            stats.movementSpeed += def.movementSpeed.getAdditionalFlat();
        }
    }

    static void AccumulateMultipliers( Stats stats, ItemStack item, EquipmentSlot slot )
    {
        // this is a bit weird, but this puts the multipliers into stats.
        // Use a fresh stat object, accumulate the multipliers, then use the final multiplier to multiply the original values.

        RPGItemData itemData = RPGItemData.getFrom( item );
        if( itemData == null )
        {
            return;
        }

        var def = RPGItemRegistry.get( itemData.getID() );
        if( def == null )
        {
            return;
        }

        if( !def.type.IsValid( slot, false ) )
        {
            return;
        }

        for( var key : def.physicalDamage.keySet() )
        {
            double value = def.physicalDamage.get( key ).getAdditionalPercent();
            if( value != 0 )
            {
                Double statsValue = stats.physicalDamage.getOrDefault( key, 0.0 );
                stats.physicalDamage.put( key, statsValue + value );
            }
        }
        for( var key : def.magicalDamage.keySet() )
        {
            double value = def.magicalDamage.get( key ).getAdditionalPercent();
            if( value != 0 )
            {
                Double statsValue = stats.magicalDamage.getOrDefault( key, 0.0 );
                stats.magicalDamage.put( key, statsValue + value );
            }
        }
        if( def.attackSpeed != null )
        {
            stats.attackSpeed += def.attackSpeed.getAdditionalPercent();
        }
        if( def.critChance != null )
        {
            stats.critChance += def.critChance.getAdditionalPercent();
        }
        for( var key : def.physicalArmor.keySet() )
        {
            double value = def.physicalArmor.get( key ).getAdditionalPercent();
            if( value != 0 )
            {
                Double statsValue = stats.physicalArmor.getOrDefault( key, 0.0 );
                stats.physicalArmor.put( key, statsValue + value );
            }
        }
        for( var key : def.magicalArmor.keySet() )
        {
            double value = def.magicalArmor.get( key ).getAdditionalPercent();
            if( value != 0 )
            {
                Double statsValue = stats.magicalArmor.getOrDefault( key, 0.0 );
                stats.magicalArmor.put( key, statsValue + value );
            }
        }
        if( def.maxHealth != null )
        {
            stats.maxHealth += def.maxHealth.getAdditionalPercent();
        }
        if( def.movementSpeed != null )
        {
            stats.movementSpeed += def.movementSpeed.getAdditionalPercent();
        }
    }

    /**
     * Calculates the stats of a given entity.
     *
     * @param entity The entity to calculate the stats of.
     * @return Null for non-rpg entities.
     */
    @Nullable
    public static Stats getFrom( Entity entity )
    {
        if( entity instanceof Player player )
        {
            Stats stats = new Stats();
            Stats multipliers = new Stats();

            var inv = player.getInventory();
            stats.maxHealth = player.getAttribute( Attribute.GENERIC_MAX_HEALTH ).getBaseValue();

            ItemStack weapon = inv.getItemInMainHand();
            AddToStatsPrimary( stats, weapon, EquipmentSlot.MAIN_HAND );
            AddToStatsAdditionalFlat( stats, weapon, EquipmentSlot.MAIN_HAND );
            AccumulateMultipliers( multipliers, weapon, EquipmentSlot.MAIN_HAND );
            ItemStack offhand = inv.getItemInOffHand();
            AddToStatsPrimary( stats, offhand, EquipmentSlot.OFF_HAND );
            AddToStatsAdditionalFlat( stats, offhand, EquipmentSlot.OFF_HAND );
            AccumulateMultipliers( multipliers, offhand, EquipmentSlot.OFF_HAND );
            ItemStack helmet = inv.getHelmet();
            AddToStatsPrimary( stats, helmet, EquipmentSlot.HEAD );
            AddToStatsAdditionalFlat( stats, helmet, EquipmentSlot.HEAD );
            AccumulateMultipliers( multipliers, helmet, EquipmentSlot.HEAD );
            ItemStack chestplate = inv.getChestplate();
            AddToStatsPrimary( stats, chestplate, EquipmentSlot.CHEST );
            AddToStatsAdditionalFlat( stats, chestplate, EquipmentSlot.CHEST );
            AccumulateMultipliers( multipliers, chestplate, EquipmentSlot.CHEST );
            ItemStack leggings = inv.getLeggings();
            AddToStatsPrimary( stats, leggings, EquipmentSlot.LEGS );
            AddToStatsAdditionalFlat( stats, leggings, EquipmentSlot.LEGS );
            AccumulateMultipliers( multipliers, leggings, EquipmentSlot.LEGS );
            ItemStack boots = inv.getBoots();
            AddToStatsPrimary( stats, boots, EquipmentSlot.FEET );
            AddToStatsAdditionalFlat( stats, boots, EquipmentSlot.FEET );
            AccumulateMultipliers( multipliers, boots, EquipmentSlot.FEET );

            stats.multiplyBy( multipliers );

            return stats;
        }

        RPGEntityData rpgEntityData = RPGEntityData.getFrom( entity );
        if( rpgEntityData != null )
        {
            RPGEntityDef def = RPGEntityRegistry.get( rpgEntityData.getID() );
            if( def != null )
            {
                Stats stats = new Stats();
                stats.maxHealth = def.maxHealth;
                stats.physicalDamage = new HashMap<>();
                for( var key : def.physicalDamage.keySet() )
                {
                    stats.physicalDamage.put( key, def.physicalDamage.get( key ) );
                }
                stats.magicalDamage = new HashMap<>();
                for( var key : def.magicalDamage.keySet() )
                {
                    stats.magicalDamage.put( key, def.magicalDamage.get( key ) );
                }
                stats.critChance = def.critChance;
                stats.attackSpeed = null;
                stats.physicalArmor = new HashMap<>();
                for( var key : def.physicalArmor.keySet() )
                {
                    stats.physicalArmor.put( key, def.physicalArmor.get( key ) );
                }
                stats.magicalArmor = new HashMap<>();
                for( var key : def.magicalArmor.keySet() )
                {
                    stats.magicalArmor.put( key, def.magicalArmor.get( key ) );
                }
                stats.aggroRange = def.aggroRange;
                stats.movementSpeed = def.movementSpeed;
                stats.knockbackResistance = def.knockbackResistance;

                return stats;
            }
        }

        return null;
    }
}
