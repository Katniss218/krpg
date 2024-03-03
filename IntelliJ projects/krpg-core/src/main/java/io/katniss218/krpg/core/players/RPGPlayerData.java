package io.katniss218.krpg.core.players;

import java.util.UUID;

public class RPGPlayerData
{
    private boolean shouldSave;
    private final UUID uuid;

    private int level = 1;
    private double xp = 0;

    public RPGPlayerData( UUID uuid )
    {
        this.shouldSave = true;
        this.uuid = uuid;
    }

    public boolean shouldSave()
    {
        return shouldSave;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel( int level )
    {
        this.level = level;
        this.shouldSave = true;
    }

    public double getXp()
    {
        return xp;
    }

    public void setXp( double xp )
    {
        this.xp = xp;
        this.shouldSave = true;
    }
}