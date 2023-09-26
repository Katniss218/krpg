package io.katniss218.krpg.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.Instant;

/**
 * Specifies persistent data about an RPG entity.
 */
public class RPGEntityData
{
    String id;
    Location spawnLocation;
    Instant spawnTime;

    public String getID()
    {
        return this.id;
    }

    public void setID( String id )
    {
        this.id = id;
    }

    public Location getSpawnLocation()
    {
        return this.spawnLocation;
    }

    public void setSpawnLocation( Location spawnLocation )
    {
        this.spawnLocation = spawnLocation;
    }

    public Instant getSpawnTime()
    {
        return this.spawnTime;
    }

    public void setSpawnTime( Instant spawnTime )
    {
        this.spawnTime = spawnTime;
    }

    private String asString()
    {
        return "rpgentity|"
                + this.id + "|"
                + this.spawnLocation.getX() + "," + this.spawnLocation.getY() + "," + this.spawnLocation.getZ() + "," + this.spawnLocation.getWorld().getName()
                + this.spawnTime.toString();
    }

    private static RPGEntityData valueOf( String s )
    {
        String[] split = s.split( "\\|" );
        RPGEntityData data = new RPGEntityData();
        String[] splitWorld = split[2].split( "," );
        data.id = split[1];
        data.spawnLocation = new Location(
                Bukkit.getWorld( splitWorld[3] ),
                Double.valueOf( splitWorld[0] ),
                Double.valueOf( splitWorld[1] ),
                Double.valueOf( splitWorld[2] ) );
        data.spawnTime = Instant.parse( split[3] );
        return data;
    }
}