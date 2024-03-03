package io.katniss218.krpg.core.spawners;

import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RPGSpawnerCommand implements TabExecutor
{
    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args )
    {
        if( args.length >= 1 && args[0].equals( "list" ) )
        {

        }
        if( args.length >= 1 && args[0].equals( "create" ) )
        {

        }
        if( args.length >= 1 && args[0].equals( "modify" ) )
        {

        }
        if( args.length >= 1 && args[0].equals( "remove" ) )
        {

        }
        if( args.length >= 1 && args[0].equals( "tp" ) )
        {

        }
        // returning false makes the "usage" show up. we don't want that.
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args )
    {
        if( args.length <= 1 )
        {
            // completing first arg.
            return Arrays.asList( "get", "sync", "reload" );
        }
        return new ArrayList<>();
    }
}