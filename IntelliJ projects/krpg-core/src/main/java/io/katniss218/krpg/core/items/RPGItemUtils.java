package io.katniss218.krpg.core.items;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.katniss218.krpg.core.RPGItemData;
import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RPGItemUtils
{
    @Nonnull
    @Contract( pure = true )
    public static ItemStack syncItem( @Nonnull ItemStack item )
    {
        var nmsItemStack = CraftItemStack.asNMSCopy( item );

        RPGItemData persistentData = RPGItemData.getFrom( item );
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

        if( def.nbt == null )
        {
            compound = new CompoundTag();
        }
        else
        {
            try
            {
                compound = net.minecraft.nbt.TagParser.parseTag( def.nbt );
            }
            catch( CommandSyntaxException e )
            {
                compound = new CompoundTag();
            }
        }

        compound.putInt( "HideFlags", Integer.MAX_VALUE );
        compound.put( "AttributeModifiers", getAttributeModifiers( def ) );

        nmsItemStack.setTag( compound );

        // todo - slot hash attack speed.

        // todo

        ItemStack newItem = CraftItemStack.asBukkitCopy( nmsItemStack );

        // Use a clear meta.
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta( item.getType() );

        meta.setCustomModelData( def.modelData );
        List<Component> loreLines = new ArrayList<>();
        loreLines.add( Component.text( "A" ) );
        meta.lore( loreLines );
        meta.displayName( Component.text( def.displayName ) );

        newItem.setItemMeta( meta );

        return newItem;
    }

    @NotNull
    private static ListTag getAttributeModifiers( RPGItemDef def )
    {
        ListTag attributesTag = new ListTag();
        CompoundTag attributeTag = new CompoundTag();
        attributeTag.putString( "AttributeName", "generic.attack_speed" );
        attributeTag.putString( "AttributeName", "generic.attack_speed" );
        attributeTag.putDouble( "Amount", def.attackSpeed.getPrimary() + def.attackSpeed.getAdditionalFlat() );
        attributeTag.putInt( "Operation", 0 );
        attributeTag.putUUID( "UUID", def.type.getSlotUUID() );
        attributesTag.add( attributeTag );
        return attributesTag;
    }
}