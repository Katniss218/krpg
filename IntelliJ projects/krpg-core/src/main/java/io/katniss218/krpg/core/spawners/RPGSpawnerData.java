package io.katniss218.krpg.core.spawners;

import org.bukkit.Location;

/**
 * Holds persistent data about a spawner existing somewhere in the world.
 */
public class RPGSpawnerData
{
    private int id;
    private boolean shouldSave;

    private Location location;
    private String entityId;

    private int minCount;
    private int maxCount;

    public RPGSpawnerData( int id )
    {
        this.id = id;
    }

    public boolean shouldSave()
    {
        return shouldSave;
    }

    public int getId()
    {
        return id;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation( Location location )
    {
        this.location = location;
        this.shouldSave = true;
    }

    public String getEntityId()
    {
        return entityId;
    }

    public void setEntityId( String entityId )
    {
        this.entityId = entityId;
        this.shouldSave = true;
    }

    public int getMinCount()
    {
        return minCount;
    }

    public void setMinCount( int minCount )
    {
        this.minCount = minCount;
        this.shouldSave = true;
    }

    public int getMaxCount()
    {
        return maxCount;
    }

    public void setMaxCount( int maxCount )
    {
        this.maxCount = maxCount;
        this.shouldSave = true;
    }
}