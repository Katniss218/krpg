package io.katniss218.krpg.core.nbt;

import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityNBT
{
    @Nullable
    public static String getEntityID( @Nonnull Entity entity )
    {
        var nmsEntity = ((CraftEntity)entity).getHandle();

        CompoundTag tag = new CompoundTag();
        nmsEntity.save( tag );

        CollectionTag<StringTag> x = (CollectionTag<StringTag>)tag.get( "Tags" );
        StringTag val = x.get( 0 );
        return val.getAsString();
    }

    public static void setEntityID( @Nonnull Entity entity, @Nonnull String id )
    {
        var nmsEntity = ((CraftEntity)entity).getHandle();

        CompoundTag tag = new CompoundTag();
        nmsEntity.save( tag );
        // possibly can use getTags and addTag on nmsentity

        ListTag x = new net.minecraft.nbt.ListTag();
        x.add( StringTag.valueOf( id ) );
        tag.put( "Tags", x );
        nmsEntity.load( tag );
    }
}