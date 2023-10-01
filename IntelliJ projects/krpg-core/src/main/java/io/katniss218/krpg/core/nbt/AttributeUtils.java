package io.katniss218.krpg.core.nbt;

import io.katniss218.krpg.core.definitions.RPGItemDef;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public final class AttributeUtils
{
    @Nonnull
    public static CompoundTag createAttributeModifier( @Nonnull String attributeName, double amount, int operation, @Nonnull UUID uuid )
    {
        CompoundTag tag = new CompoundTag();
        tag.putString( "Name", attributeName );
        tag.putString( "AttributeName", attributeName );
        tag.putDouble( "Amount", amount );
        tag.putInt( "Operation", operation );
        tag.putUUID( "UUID", uuid );
        return tag;
    }

    @Nonnull
    public static CompoundTag createAttribute( @Nonnull String attributeName, double amount )
    {
        CompoundTag tag = new CompoundTag();
        tag.putString( "Name", attributeName );
        tag.putDouble( "Base", amount );
        return tag;
    }
}
