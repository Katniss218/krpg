package io.katniss218.krpg.core.definitions;

import org.bukkit.Material;

public class EquipmentDef
{
    private Material material;
    private String nbt;

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial( Material material )
    {
        this.material = material;
    }

    public String getNbt()
    {
        return nbt;
    }

    public void setNbt( String nbt )
    {
        this.nbt = nbt;
    }

    public EquipmentDef( Material material, String nbt )
    {
        this.material = material;
        this.nbt = nbt;
    }
}
