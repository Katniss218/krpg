package io.katniss218.krpg.core;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Specifies persistent data about an RPG item.
 */
public class RPGItemData
{
    final static String TAG_NAME_ID = "rpg.item_id";
    final static String TAG_NAME_PREFIX = "rpg.prefix_id";
    final static String TAG_NAME_DURABILITY_REMAINING = "rpg.durability";

    String id;
    String prefixId;
    public int durabilityRemaining;

    public String getID()
    {
        return this.id;
    }

    public void setID( String id )
    {
        this.id = id;
    }

    @Nullable
    public static RPGItemData getFrom( @Nonnull ItemStack item )
    {
        var nmsItemStack = CraftItemStack.asNMSCopy( item );

        var compound = nmsItemStack.getTag();
        if( compound == null )
        {
            return null;
        }

        RPGItemData data = new RPGItemData();
        data.id = compound.getString( TAG_NAME_ID );
        data.prefixId = compound.getString( TAG_NAME_PREFIX );
        data.durabilityRemaining = compound.getInt( TAG_NAME_DURABILITY_REMAINING );

        return data;
    }

    @Nonnull
    public ItemStack applyToCopy( @Nonnull ItemStack item )
    {
        var nmsItemStack = CraftItemStack.asNMSCopy( item );
        var compound = nmsItemStack.getTag();

        if( compound == null )
        {
            compound = new CompoundTag();
        }

        compound.putString( TAG_NAME_ID, this.id );
        compound.putString( TAG_NAME_PREFIX, this.prefixId );
        compound.putInt( TAG_NAME_DURABILITY_REMAINING, this.durabilityRemaining );

        nmsItemStack.setTag( compound );
        return CraftItemStack.asBukkitCopy( nmsItemStack );
    }
}