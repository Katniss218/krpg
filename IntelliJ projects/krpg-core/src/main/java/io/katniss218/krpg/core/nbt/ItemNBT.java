package io.katniss218.krpg.core.nbt;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemNBT
{
    private final static String TAG_NAME_ID = "rpg.item_id";

    @Nullable
    @Contract( pure = true )
    public static String getItemID( @Nonnull ItemStack item )
    {
        var nmsItemStack = CraftItemStack.asNMSCopy( item );
        if( !nmsItemStack.hasTag() )
        {
            return null;
        }

        var compound = nmsItemStack.getTag();
        if( compound == null )
        {
            return null;
        }

        return compound.getString( TAG_NAME_ID );
    }

    @Nonnull
    public static ItemStack setItemID( @Nonnull ItemStack item, @Nonnull String id )
    {
        var nmsItemStack = CraftItemStack.asNMSCopy( item );
        var compound = nmsItemStack.getTag();

        if( compound == null )
            compound = new CompoundTag();

        compound.putString( TAG_NAME_ID, id );

        nmsItemStack.setTag( compound );
        return CraftItemStack.asBukkitCopy( nmsItemStack );
    }
}