package io.katniss218.krpg.core.items;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.nbt.AttributeUtils;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is concerned with creating in-game item stacks from RPG item definitions and data.
 */
public final class RPGItemFactory
{
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
            data = new RPGItemData();
            data.setID( def.id );

            data.durabilityRemaining = Objects.requireNonNullElse( def.durability, Integer.MAX_VALUE );
        }

        ItemStack item = new ItemStack( def.baseItem, amount );

        // Use a clear meta.
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta( item.getType() );

        meta.setCustomModelData( def.modelData );
        List<Component> loreLines = new ArrayList<>();
        for( final var line : def.displayDescription )
        {
            loreLines.add( Component.text( line ) ); // TODO - minimessage format colors
        }
        meta.lore( loreLines );
        meta.displayName( Component.text( "Lv." + def.level + " " + def.displayName ) ); // TODO - minimessage format colors

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

        // todo - slot hash attack speed.

        // todo

        return CraftItemStack.asBukkitCopy( nmsItemStack );
    }

    @Nonnull
    @Contract( pure = true )
    public static ItemStack syncItemStack( @Nonnull ItemStack item )
    {
        var nmsItemStack = CraftItemStack.asNMSCopy( item );

        RPGItemData persistentData = RPGItemData.getFrom( nmsItemStack );
        if( persistentData == null )
        {
            return item;
        }

        var compound = nmsItemStack.getTag();
        if( compound == null )
        {
            return item;
        }

        RPGItemDef def = RPGItemRegistry.get( persistentData.getID() );
        if( def == null )
        {
            return item;
        }

        return createItemStack( def, item.getAmount(), persistentData );
    }
}