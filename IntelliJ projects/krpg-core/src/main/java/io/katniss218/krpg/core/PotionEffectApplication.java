package io.katniss218.krpg.core;

import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class PotionEffectApplication
{
    private double chance;
    private PotionEffectType effectType;
    private int duration;
    private short amplifier;

    public double getChance()
    {
        return chance;
    }

    public void setChance( @Range( from = 0, to = 1 ) double chance )
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

    public int getDuration()
    {
        return duration;
    }

    public void setDuration( @Range( from = 0, to = Integer.MAX_VALUE ) int duration )
    {
        this.duration = duration;
    }

    public short getAmplifier()
    {
        return amplifier;
    }

    public void setAmplifier( @Range( from = 0, to = 255 ) short amplifier )
    {
        this.amplifier = amplifier;
    }

    @Contract( pure = true )
    public static PotionEffectApplication valueOf( @NotNull String s )
    {
        try
        {
            String[] split = s.split( "\\|" );
            PotionEffectApplication p = new PotionEffectApplication();
            p.setDuration( Integer.parseUnsignedInt( split[0] ) );
            p.setEffectType( PotionEffectType.getByName( split[1] ) );
            p.setAmplifier( (short)Integer.parseUnsignedInt( split[2] ) );
            p.setChance( Double.parseDouble( split[3] ) );
            return p;
        }
        catch( Exception ex )
        {
            throw new IllegalArgumentException("provided string '" + s + "' was not valid for a PotionEffectApplication.", ex);
        }
    }
}
