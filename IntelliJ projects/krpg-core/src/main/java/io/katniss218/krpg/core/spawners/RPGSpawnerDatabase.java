package io.katniss218.krpg.core.spawners;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGEntityDef;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.entities.RPGEntityFactory;
import io.katniss218.krpg.core.loottables.LootTableDropper;
import io.katniss218.krpg.core.players.RPGPlayerData;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.*;

public class RPGSpawnerDatabase
{

    public static class Ticker extends BukkitRunnable
    {
        @Override
        public void run()
        {
            RPGSpawnerDatabase.tick();
        }
    }

    private final static String dbName = "spawnerdata.db";

    private static HashMap<Integer, RPGSpawnerData> spawners = new HashMap<>();

    public static Collection<RPGSpawnerData> getSpawners()
    {
        return spawners.values();
    }

    @Nullable
    public static RPGSpawnerData get( @Nonnull Location location )
    {
        for( final var data : spawners.values() )
        {
            if( data.getLocation().equals( location ) )
            {
                return data;
            }
        }
        return null;
    }

    public static int nextId()
    {
        return spawners.size() + 1;
    }

    public static void add( RPGSpawnerData spawner )
    {
        spawners.put( spawner.getId(), spawner );
    }

    public static boolean remove( Location location )
    {
        RPGSpawnerData spawner = null;
        for( final var data : spawners.values() )
        {
            if( data.getLocation().equals( location ) )
            {
                spawner = data;
                break;
            }
        }
        if( spawner != null )
            return spawners.remove( spawner.getId() ) != null;
        return false;
    }

    public static void tick()
    {
        final var onlinePlayers = Bukkit.getOnlinePlayers();

        for( final var spawner : spawners.values() )
        {
            if( !spawner.getLocation().isChunkLoaded() )
                continue;

            KRPGCore.getPluginLogger().info( "A" );

            boolean inRange = false;
            for( final var player : onlinePlayers )
            {
                if( player.getLocation().distance( spawner.getLocation() ) < 80 )
                {
                    inRange = true;
                    break;
                }
            }

            if( inRange )
            {
                KRPGCore.getPluginLogger().info( "B" );
                final var entities = spawner.getLocation().getNearbyEntities( 24, 24, 24 );

                boolean rpgEntityInRange = false;
                for( final var entity : entities )
                {
                    if( entity instanceof org.bukkit.entity.Item )
                        continue;
                    if( entity instanceof org.bukkit.entity.Arrow )
                        continue;
                    if( entity instanceof org.bukkit.entity.Painting )
                        continue;
                    if( entity instanceof org.bukkit.entity.ItemFrame )
                        continue;
                    if( entity instanceof org.bukkit.entity.ArmorStand )
                        continue;
                    if( entity instanceof org.bukkit.entity.Boat )
                        continue;

                    rpgEntityInRange = true;
                    break;
                }

                if( !rpgEntityInRange )
                {
                    KRPGCore.getPluginLogger().info( "C" );
                    RPGEntityDef def = RPGEntityRegistry.get( spawner.getEntityId() );
                    KRPGCore.getPluginLogger().info( spawner.getEntityId() );
                    if( def != null )
                    {
                        KRPGCore.getPluginLogger().info( "D" );
                        var entity = RPGEntityFactory.createEntity( def, spawner.getLocation(), LootTableDropper.randomRange( spawner.getMinCount(), spawner.getMaxCount() + 1 ), null );
                    }
                }
            }
        }
    }

    public static void SaveToDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName( "org.sqlite.JDBC" );
        Connection connection = DriverManager.getConnection( "jdbc:sqlite:plugins/KRPG-Core/" + dbName );

        Statement statement = connection.createStatement();
        statement.execute( """
                CREATE TABLE IF NOT EXISTS spawners (id INTEGER PRIMARY KEY, x REAL, y REAL, z REAL, world STRING, entityId STRING, minCount INTEGER, maxCount INTEGER )""" );

        PreparedStatement insertPlayerStatement = connection.prepareStatement(
                "INSERT OR REPLACE INTO spawners (id, x, y, z, world, entityId, minCount, maxCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)" );

        for( final var data : spawners.values() )
        {
            if( !data.shouldSave() )
                continue;

            try
            {
                insertPlayerStatement.setInt( 1, data.getId() );
                insertPlayerStatement.setDouble( 2, data.getLocation().getX() );
                insertPlayerStatement.setDouble( 3, data.getLocation().getY() );
                insertPlayerStatement.setDouble( 4, data.getLocation().getZ() );
                insertPlayerStatement.setString( 5, data.getLocation().getWorld().getName() );
                insertPlayerStatement.setString( 6, data.getEntityId() );
                insertPlayerStatement.setInt( 7, data.getMinCount() );
                insertPlayerStatement.setInt( 8, data.getMaxCount() );

                insertPlayerStatement.executeUpdate();
            }
            catch( Exception ex )
            {
                KRPGCore.getPluginLogger().warning( "Couldn't save an entry to '" + dbName + "' database: " + ex.getMessage() );
            }
        }
        connection.close();
    }

    public static void LoadFromDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName( "org.sqlite.JDBC" );
        Connection connection = DriverManager.getConnection( "jdbc:sqlite:plugins/KRPG-Core/" + dbName );

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery( """
                SELECT id, x, y, z, world, entityId, minCount, maxCount FROM spawners""" );

        spawners.clear();
        while( resultSet.next() )
        {
            try
            {
                int id = resultSet.getInt( "id" );
                double x = resultSet.getDouble( "x" );
                double y = resultSet.getDouble( "y" );
                double z = resultSet.getDouble( "z" );
                String worldName = resultSet.getString( "world" );
                String entityId = resultSet.getString( "entityId" );
                int minCount = resultSet.getInt( "minCount" );
                int maxCount = resultSet.getInt( "maxCount" );

                RPGSpawnerData data = new RPGSpawnerData( id );
                // FIXME - GetWorld is called before world is loaded.
                data.setLocation( new Location( Bukkit.getWorld( worldName ), x, y, z, 0, 0 ) );
                data.setEntityId( entityId );
                data.setMinCount( minCount );
                data.setMaxCount( maxCount );
                spawners.put( data.getId(), data );
            }
            catch( Exception ex )
            {
                KRPGCore.getPluginLogger().warning( "Couldn't load an entry from '" + dbName + "' database: " + ex.getMessage() );
            }
        }
    }
}