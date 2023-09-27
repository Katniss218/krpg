package io.katniss218.krpg.core;

import org.bukkit.potion.PotionEffectType;

public class PotionEffectDef
{
    private double chance;
    private PotionEffectType effectType;
    private double duration;
    private double amplifier;

    public double getChance()
    {
        return chance;
    }

    public void setChance( double chance )
    {
        this.chance = chance;
    }

    public PotionEffectType getEffectType()
    {
        return effectType;
    }

    public void setEffectType( PotionEffectType effectType )
    {
        this.effectType = effectType;
    }

    public double getDuration()
    {
        return duration;
    }

    public void setDuration( double duration )
    {
        this.duration = duration;
    }

    public double getAmplifier()
    {
        return amplifier;
    }

    public void setAmplifier( double amplifier )
    {
        this.amplifier = amplifier;
    }
}
