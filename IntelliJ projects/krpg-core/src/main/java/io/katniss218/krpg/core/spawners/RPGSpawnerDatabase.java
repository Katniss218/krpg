package io.katniss218.krpg.core.spawners;

import org.bukkit.World;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RPGSpawnerDatabase
{
    List<RPGSpawnerData> spawners = new ArrayList<>();

    public void tick()
    {

    }

    public void connect() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:plugins/KRPG-Core/database.db");

        Statement statement = connection.createStatement();
        statement.execute( """
                CREATE TABLE IF NOT EXISTS players (uuid STRING PRIMARY KEY, level INTEGER, xp REAL)""");

        connection.close();
    }
}
