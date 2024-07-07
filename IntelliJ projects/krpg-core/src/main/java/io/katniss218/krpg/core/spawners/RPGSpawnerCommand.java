package io.katniss218.krpg.core.spawners;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.definitions.RPGLootTableRegistry;
import io.katniss218.krpg.core.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
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
            if( !sender.hasPermission( "rpg." + command.getName() + ".list" ))
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }

            for( final var data : RPGSpawnerDatabase.getSpawners())
            {
                sender.sendMessage( ColorUtils.GetComponent( "&a- " + data.getId() + "&8: &6" + data.getLocation().getX() + ", " + data.getLocation().getY() + ", " + data.getLocation().getZ() + " in " + data.getLocation().getWorld().getName() + "&8 - &a" + data.getMinCount() + "-" + data.getMaxCount() + "x " + data.getEntityId() ) );
            }
        }
        if( args.length >= 1 && args[0].equals( "create" ) )
        {
            if( !sender.hasPermission( "rpg." + command.getName() + ".create" ) )
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }
            if( sender instanceof Player player )
            {
                if( args.length >= 2 )
                {
                    int count = 1;
                    if( args.length >= 3 )
                        count = Integer.parseInt( args[2] );

                    Location roundLoc = new Location(
                            player.getLocation().getWorld(),
                            Math.floor( player.getLocation().getX() ) + 0.5,
                            Math.floor( player.getLocation().getY() ) + 0.5,
                            Math.floor( player.getLocation().getZ() ) + 0.5 );

                    if( RPGSpawnerDatabase.get( roundLoc ) != null )
                    {
                        player.sendMessage( ColorUtils.GetComponent( "&c" + roundLoc + "is already a spawner." ) );
                        return true;
                    }

                    RPGSpawnerData data = new RPGSpawnerData( RPGSpawnerDatabase.nextId() );
                    data.setEntityId( args[1] );
                    data.setMinCount( count );
                    data.setMaxCount( count );
                    data.setLocation( roundLoc );

                    RPGSpawnerDatabase.add( data );
                }
            }
        }
        if( args.length >= 1 && args[0].equals( "modify" ) )
        {

        }
        if( args.length >= 1 && args[0].equals( "remove" ) )
        {
            if( !sender.hasPermission( "rpg." + command.getName() + ".remove" ) )
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }
            if( sender instanceof Player player )
            {
                Location roundLoc = new Location(
                        player.getLocation().getWorld(),
                        Math.floor( player.getLocation().getX() ) + 0.5,
                        Math.floor( player.getLocation().getY() ) + 0.5,
                        Math.floor( player.getLocation().getZ() ) + 0.5 );

                RPGSpawnerDatabase.remove( roundLoc );
            }
        }
        if( args.length >= 1 && args[0].equals( "tp" ) )
        {

        }
        if( args.length >= 1 && args[0].equals( "tick" ) )
        {
            RPGSpawnerTicker.tickAll();
        }
        if( args.length >= 1 && args[0].equals( "reload" ) )
        {
            try
            {
                RPGSpawnerDatabase.LoadFromDatabase();
            }
            catch( Exception ex )
            {
                KRPGCore.getPluginLogger().info( ex.getMessage() );
            }
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
            return Arrays.asList( "list", "create"/*, "modify"*/, "remove", "tp" );
        }
        if( args.length == 2 && args[0].equals( "create" ) )
        {
            return RPGEntityRegistry.getRegisteredIDs();
        }
        return new ArrayList<>();
    }
}