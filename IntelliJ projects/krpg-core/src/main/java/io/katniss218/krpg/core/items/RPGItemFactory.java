package io.katniss218.krpg.core.items;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.PhysicalDamageType;
import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.definitions.RPGRarityDef;
import io.katniss218.krpg.core.definitions.RPGRarityRegistry;
import io.katniss218.krpg.core.nbt.AttributeUtils;
import io.katniss218.krpg.core.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is concerned with creating in-game item stacks from RPG item definitions and data.
 */
public final class RPGItemFactory
{
    private static final DecimalFormat decimalFormat = new DecimalFormat( "#.#" );

    public static void AppendProperty( @Nonnull List<Component> loreLines, @Nonnull String displayName, @Nullable String displayType, double primary, double flat, double percent )
    {
        if( primary != 0 )
        {
            String loreLine = displayType == null
                    ? "&8> &7&l" + displayName + " &8: " + ColorUtils.GetSignColor( primary ) + "&l" + decimalFormat.format( primary )
                    : "&8> &7&l" + displayName + " (&r&7" + displayType + "&7&l)&8: " + ColorUtils.GetSignColor( primary ) + "&l" + decimalFormat.format( primary );
            loreLines.add( ColorUtils.GetComponent( loreLine ).decoration( TextDecoration.ITALIC, false ) );
        }
        if( flat != 0 )
        {
            String loreLine = displayType == null
                    ? "&8> &7&l" + displayName + " &8: " + ColorUtils.GetSignColor( flat ) + "&l" + decimalFormat.format( flat )
                    : "&8> &7&l" + displayName + " (&r&7" + displayType + "&7&l)&8: " + ColorUtils.GetSignColor( flat ) + "&l" + decimalFormat.format( flat );
            loreLines.add( ColorUtils.GetComponent( loreLine ).decoration( TextDecoration.ITALIC, false ) );
        }
        if( percent != 0 )
        {
            String loreLine = displayType == null
                    ? "&8> &7&l" + displayName + " &8: " + ColorUtils.GetSignColor( percent ) + "&l" + decimalFormat.format( percent )
                    : "&8> &7&l" + displayName + " (&r&7" + displayType + "&7&l)&8: " + ColorUtils.GetSignColor( percent ) + "&l" + decimalFormat.format( percent * 100 ) + "%";
            loreLines.add( ColorUtils.GetComponent( loreLine ).decoration( TextDecoration.ITALIC, false ) );
        }
    }

    public static void AppendProperties( @Nonnull List<Component> loreLines, @Nonnull RPGItemDef def )
    {
        for( final var prop : def.physicalDamage.entrySet() )
        {
            AppendProperty( loreLines, "Damage", prop.getKey().toString(),
                    prop.getValue().getPrimary(), prop.getValue().getAdditionalFlat(), prop.getValue().getAdditionalPercent() );
        }
        for( final var prop : def.magicalDamage.entrySet() )
        {
            AppendProperty( loreLines, "Damage", prop.getKey().toString(),
                    prop.getValue().getPrimary(), prop.getValue().getAdditionalFlat(), prop.getValue().getAdditionalPercent() );
        }
        if( def.critChance != null )
        {
            AppendProperty( loreLines, "Crit Chance", null,
                    def.critChance.getPrimary(), def.critChance.getAdditionalFlat(), def.critChance.getAdditionalPercent() );
        }
        if( def.attackSpeed != null )
        {
            AppendProperty( loreLines, "Attack Speed", null,
                    def.attackSpeed.getPrimary(), def.attackSpeed.getAdditionalFlat(), def.attackSpeed.getAdditionalPercent() );
        }

        for( final var prop : def.physicalArmor.entrySet() )
        {
            AppendProperty( loreLines, "Armor", prop.getKey().toString(),
                    prop.getValue().getPrimary(), prop.getValue().getAdditionalFlat(), prop.getValue().getAdditionalPercent() );
        }
        for( final var prop : def.magicalArmor.entrySet() )
        {
            AppendProperty( loreLines, "Armor", prop.getKey().toString(),
                    prop.getValue().getPrimary(), prop.getValue().getAdditionalFlat(), prop.getValue().getAdditionalPercent() );
        }

        if( def.maxHealth != null )
        {
            AppendProperty( loreLines, "Max Health", null,
                    def.maxHealth.getPrimary(), def.maxHealth.getAdditionalFlat(), def.maxHealth.getAdditionalPercent() );
        }
        if( def.movementSpeed != null )
        {
            AppendProperty( loreLines, "Movement Speed", null,
                    def.movementSpeed.getPrimary(), def.movementSpeed.getAdditionalFlat(), def.movementSpeed.getAdditionalPercent() );
        }
        if( def.food != null )
        {
            AppendProperty( loreLines, "Food", null,
                    def.food, 0, 0 );
        }

        // buy/sell values if displayed in shops.
    }

    /**
     * Creates an item stack with the given amount and data.
     *
     * @param def    The RPG item definition to use as base of the item.
     * @param amount The amount of the item in the created stack.
     * @param data   The data structure to use for persistent data.
     * @return The constructed item stack.
     */
    @Nonnull
    @Contract( pure = true )
    public static ItemStack createItemStack( @Nonnull RPGItemDef def, int amount, @Nullable RPGItemData data )
    {
        if( data == null )
        {
            data = new RPGItemData( def );
        }

        ItemStack item = new ItemStack( def.baseItem, amount );

        // Use a clear meta.
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta( item.getType() );

        meta.setCustomModelData( def.modelData );

        RPGRarityDef rarity = RPGRarityRegistry.get( def.rarity );

        List<Component> loreLines = new ArrayList<>();
        for( final var line : def.displayDescription )
        {
            loreLines.add( ColorUtils.GetComponent( rarity.getSecondaryColor() + line ).decoration( TextDecoration.ITALIC, false ) );
        }
        loreLines.add( Component.text( "" ) );
        loreLines.add( ColorUtils.GetComponent( "&8: " + rarity.getPrimaryColor() + rarity.getDisplayName() + " " + def.type.toString() ).decoration( TextDecoration.ITALIC, false ) );
        loreLines.add( Component.text( "" ) );
        AppendProperties( loreLines, def );
        meta.lore( loreLines );

        meta.displayName( ColorUtils.GetComponent( "&a&lLv." + def.level + " " + rarity.getPrimaryColor() + def.displayName ).decoration( TextDecoration.ITALIC, false ) );

        if( def.durability != null )
        {
            if( meta instanceof Damageable d )
            {
                double durabilityPercent = data.durabilityRemaining / (double)def.durability;
                if( durabilityPercent < 0 )
                    durabilityPercent = 0;
                else if( durabilityPercent > 1 )
                    durabilityPercent = 1;

                d.setDamage( (int)Math.ceil( def.baseItem.getMaxDurability() * (1 - durabilityPercent) ) );
            }
        }

        item.setItemMeta( meta ); // applying meta *after* NMS stuff resets the NMS stuff applied prior.

        var nmsItemStack = CraftItemStack.asNMSCopy( item );

        var compound = nmsItemStack.getOrCreateTag();
        if( def.nbt != null )
        {
            try
            {
                var userNbt = net.minecraft.nbt.TagParser.parseTag( def.nbt );
                compound = userNbt.merge( compound ); // `a.merge(b)` replaces things in `a` with things in `b`, if duplicate.
            }
            catch( CommandSyntaxException e )
            {
            }
        }
        compound.putInt( "HideFlags", Integer.MAX_VALUE );
        ListTag list = new ListTag();
        if( def.attackSpeed != null )
        {
            double speed = def.attackSpeed.getPrimary() + def.attackSpeed.getAdditionalFlat();
            list.add( AttributeUtils.createAttributeModifier( "generic.attack_speed", speed, 0, def.type.getSlotUUID() ) );
        }
        compound.put( "AttributeModifiers", list );

        data.applyTo( compound );
        nmsItemStack.setTag( compound );

        // todo

        return CraftItemStack.asBukkitCopy( nmsItemStack );
    }

    /**
     * Creates a new RPG item instance from the data contained within the NBT of the specified RPG item.
     * Effectively creates a synced version of the item, if the definition has changed after the original was created.
     *
     * @param item The outdated version of the item.
     * @return The up-to-date version of the item.
     */
    @Nonnull
    @Contract( pure = true )
    public static ItemStack syncItemStack( @Nonnull ItemStack item )
    {
        var nmsItemStack = CraftItemStack.asNMSCopy( item );

        RPGItemData persistentData = RPGItemData.getFrom( nmsItemStack );
        if( persistentData == null )
        {
            return item; // Not RPGItem
        }

        var compound = nmsItemStack.getTag();
        if( compound == null )
        {
            return item; // Not RPGItem
        }

        RPGItemDef def = RPGItemRegistry.get( persistentData.getID() );
        if( def == null )
        {
            return item; // Not RPGItem
        }

        return createItemStack( def, item.getAmount(), persistentData );
    }

    @Nonnull
    @Contract( pure = true )
    public static ItemStack syncItemStack( @Nonnull ItemStack item, @Nonnull RPGItemData newData )
    {
        var nmsItemStack = CraftItemStack.asNMSCopy( item );

        RPGItemData persistentData = RPGItemData.getFrom( nmsItemStack );
        if( persistentData == null )
        {
            return item; // Not RPGItem
        }

        if( !persistentData.getID().equals( newData.getID() ) )
        {
            return item;
        }

        var compound = nmsItemStack.getTag();
        if( compound == null )
        {
            return item; // Not RPGItem
        }

        RPGItemDef def = RPGItemRegistry.get( newData.getID() );
        if( def == null )
        {
            return item; // Not RPGItem
        }

        return createItemStack( def, item.getAmount(), newData );
    }
}