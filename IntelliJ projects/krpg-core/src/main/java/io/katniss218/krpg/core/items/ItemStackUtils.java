package io.katniss218.krpg.core.items;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public final class ItemStackUtils
{
    public static ItemStack createItemStack( Material material, int amount, @Nullable String nbt )
    {
        ItemStack item = new ItemStack( material, amount );
        if( nbt == null )
        {
            return item;
        }

        var nmsItemStack = CraftItemStack.asNMSCopy( item );

        try
        {
            CompoundTag compound = net.minecraft.nbt.TagParser.parseTag( nbt );
            nmsItemStack.setTag( compound );
            return CraftItemStack.asBukkitCopy( nmsItemStack );
        }
        catch( CommandSyntaxException e )
        {
            return item;
        }
    }
}