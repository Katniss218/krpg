package io.katniss218.krpg.core.entities;

import io.katniss218.krpg.core.definitions.RPGEntityDef;
import io.katniss218.krpg.core.items.RPGItemData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;

/**
 * Specifies persistent data about an RPG entity.
 */
public class RPGEntityData
{
    /**
     * The ID of the entity in question.
     */
    String id;
    /**
     * The location at which the entity was spawned. Can be used to prevent it wandering too far away.
     */
    Location spawnLocation;
    /**
     * The time at which the entity was spawned. Can be used to calculate its lifetime.
     */
    Instant spawnTime;
    /**
     * The current health of the entity.
     */
    double health;

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

    public double getHealth()
    {
        return this.health;
    }

    public void setHealth( double health )
    {
        this.health = health;
    }


    @Nullable
    public static RPGEntityData getFrom( @Nonnull Entity entity )
    {
        var nmsEntity = ((CraftEntity)entity).getHandle();

        CompoundTag compound = new CompoundTag();
        nmsEntity.save( compound );
        return RPGEntityData.getFrom( compound );
    }

    @Nullable
    public static RPGEntityData getFrom( @Nonnull CompoundTag compound )
    {
        Tag tagsTag = compound.get( "Tags" );
        if( tagsTag == null )
        {
            return null;
        }
        if( !(tagsTag instanceof ListTag listTags) )
        {
            return null;
        }

        String str = listTags.getString( 0 );
        if( str.isEmpty() )
        {
            return null;
        }

        try
        {
            return RPGEntityData.valueOf( str );
        }
        catch( Exception ex )
        {
            return null;
        }
    }

    @Contract( pure = false )
    public void applyTo( @Nonnull CompoundTag compound )
    {
        String str = this.asString();

        ListTag listTags = new net.minecraft.nbt.ListTag();
        listTags.add( StringTag.valueOf( this.asString() ) );
        compound.put( "Tags", listTags );
    }

    private String asString()
    {
        // Order matters for backwards compatibility.
        return "rpgentity"
                + "|" + this.id
                + "|" + this.spawnLocation.getX() + "," + this.spawnLocation.getY() + "," + this.spawnLocation.getZ() + "," + this.spawnLocation.getWorld().getName()
                + "|" + this.spawnTime.toString()
                + "|" + this.health;
    }

    private static RPGEntityData valueOf( String s )
    {
        // Order matters for backwards compatibility.
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
        data.health = Double.parseDouble( split[4] );
        return data;
    }
}