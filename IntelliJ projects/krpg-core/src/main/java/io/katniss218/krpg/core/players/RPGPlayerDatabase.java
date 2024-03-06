package io.katniss218.krpg.core.players;

import io.katniss218.krpg.core.KRPGCore;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public final class RPGPlayerDatabase
{
    private final static String dbName = "playerdata.db";

    private final static HashMap<UUID, RPGPlayerData> players = new HashMap<>();

    @Nonnull
    public static RPGPlayerData getOrDefault( Player player )
    {
        UUID uuid = player.getUniqueId();
        return players.computeIfAbsent( uuid, key -> new RPGPlayerData( uuid ) );
    }

    public static void set( @Nonnull RPGPlayerData data )
    {
        players.put( data.getUuid(), data );
    }

    public static void SaveToDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName( "org.sqlite.JDBC" );
        Connection connection = DriverManager.getConnection( "jdbc:sqlite:plugins/KRPG-Core/" + dbName );

        Statement statement = connection.createStatement();
        statement.execute( """
                CREATE TABLE IF NOT EXISTS players (uuid STRING PRIMARY KEY, level INTEGER, xp REAL)""" );

        PreparedStatement insertPlayerStatement = connection.prepareStatement(
                "INSERT OR REPLACE INTO players (uuid, level, xp) VALUES (?, ?, ?)");

        for( final var data : players.values() )
        {
            if( !data.shouldSave() )
                continue;

            try
            {
                insertPlayerStatement.setString(1, data.getUuid().toString());
                insertPlayerStatement.setInt(2, data.getLevel());
                insertPlayerStatement.setDouble(3, data.getXp());

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
                SELECT uuid, level, xp FROM players""" );

        players.clear();
        while( resultSet.next() )
        {
            try
            {
                UUID uuid = UUID.fromString( resultSet.getString( "uuid" ) );
                int level = resultSet.getInt( "level" );
                double xp = resultSet.getDouble( "xp" );

                RPGPlayerData data = new RPGPlayerData( uuid );
                data.setLevel( level );
                data.setXp( xp );
                players.put( uuid, data );
            }
            catch( Exception ex )
            {
                KRPGCore.getPluginLogger().warning( "Couldn't load an entry from '" + dbName + "' database: " + ex.getMessage() );
            }
        }
    }
}