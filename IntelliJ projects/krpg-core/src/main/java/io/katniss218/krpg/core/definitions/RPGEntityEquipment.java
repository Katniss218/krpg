package io.katniss218.krpg.core.definitions;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RPGEntityEquipment
{
    @Nonnull
    private Material material;
    @Nullable
    private String nbt;

    @Nonnull
    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial( @Nonnull Material material )
    {
        this.material = material;
    }

    @Nullable
    public String getNbt()
    {
        return nbt;
    }

    public void setNbt( @Nullable String nbt )
    {
        this.nbt = nbt;
    }

    public RPGEntityEquipment( @Nonnull Material material, @Nullable String nbt )
    {
        this.material = material;
        this.nbt = nbt;
    }
}
