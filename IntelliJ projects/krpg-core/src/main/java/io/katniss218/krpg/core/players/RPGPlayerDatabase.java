package io.katniss218.krpg.core.players;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public final class RPGPlayerDatabase
{
    final static String dbName = "playerdata.db";

    final static HashMap<UUID, RPGPlayerData> players = new HashMap<>();

    public static RPGPlayerData get( Player player )
    {
        UUID uuid = player.getUniqueId();
        return players.computeIfAbsent( uuid, key -> new RPGPlayerData( uuid ) );
    }

    public static void SaveToDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName( "org.sqlite.JDBC" );
        Connection connection = DriverManager.getConnection( "jdbc:sqlite:plugins/KRPG-Core/" + dbName );

        Statement statement = connection.createStatement();
        statement.execute( """
                CREATE TABLE IF NOT EXISTS players (uuid STRING PRIMARY KEY, level INTEGER, xp REAL)""" );

        for( final var data : players.values() )
        {
            if( !data.shouldSave() )
                continue;

            try
            {
                // save
            }
            catch( Exception ex )
            {
                //
            }
        }
        connection.close();
    }

    public static void LoadFromDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName( "org.sqlite.JDBC" );
        Connection connection = DriverManager.getConnection( "jdbc:sqlite:plugins/KRPG-Core/" + dbName );

        Statement statement = connection.createStatement();
        statement.execute( """
                SELECT uuid, level, xp FROM players""" );

    }
}