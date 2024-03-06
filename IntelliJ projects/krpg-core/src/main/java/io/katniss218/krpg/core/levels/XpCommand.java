package io.katniss218.krpg.core.levels;

import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.players.RPGPlayerData;
import io.katniss218.krpg.core.players.RPGPlayerDatabase;
import io.katniss218.krpg.core.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XpCommand implements TabExecutor
{

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args )
    {
        if( args.length == 0 ) // view self
        {
            if( sender instanceof Player player )
            {
                RPGPlayerData data = RPGPlayerDatabase.getOrDefault( player );
                sender.sendMessage( ColorUtils.GetComponent( "&aYour level: " + data.getLevel() + "." ) );
                sender.sendMessage( ColorUtils.GetComponent( "&aYour xp: " + data.getXp() + " / " + LevelUtils.getXpToLevel( data.getLevel() ) + "." ) );
            }
        }
        if( args.length >= 1 && args[0].equals( "reset" ) )
        {
            if( args.length >= 2 )
            {
                Player target = Bukkit.getPlayer( args[1] );
                if( target != null )
                {
                    RPGPlayerDatabase.set( new RPGPlayerData( target.getUniqueId() ) );
                    sender.sendMessage( ColorUtils.GetComponent( "&aReset experience of " + target.getName() + "." ) );
                }
            }
            else if( sender instanceof Player player )
            {
                RPGPlayerDatabase.set( new RPGPlayerData( player.getUniqueId() ) );
                sender.sendMessage( ColorUtils.GetComponent( "&aReset experience of " + player.getName() + "." ) );
            }
        }
        if( args.length >= 1 && args[0].equals( "add" ) )
        {
            if( args.length == 3 )
            {
                Player target = Bukkit.getPlayer( args[1] );
                if( target != null )
                {
                    double xp = Double.parseDouble( args[2] );
                    LevelUtils.AddXp( target, xp );
                    sender.sendMessage( ColorUtils.GetComponent( "&aAdded " + xp + " experience to " + target.getName() + "." ) );
                }
            }
            else if( sender instanceof Player player )
            {
                if( args.length == 2 )
                {
                    double xp = Double.parseDouble( args[1] );
                    LevelUtils.AddXp( player, xp );
                    sender.sendMessage( ColorUtils.GetComponent( "&aAdded " + xp + " experience to " + player.getName() + "." ) );
                }
            }
        }
        if( args.length >= 1 && args[0].equals( "remove" ) )
        {
            if( args.length == 3 )
            {
                Player target = Bukkit.getPlayer( args[1] );
                if( target != null )
                {
                    double xp = Double.parseDouble( args[2] );
                    LevelUtils.AddXp( target, -xp );
                    sender.sendMessage( ColorUtils.GetComponent( "&aRemoved " + xp + " experience from " + target.getName() + "." ) );
                }
            }
            else if( sender instanceof Player player )
            {
                if( args.length == 2 )
                {
                    double xp = Double.parseDouble( args[1] );
                    LevelUtils.AddXp( player, -xp );
                    sender.sendMessage( ColorUtils.GetComponent( "&aRemoved " + xp + " experience from " + player.getName() + "." ) );
                }
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
            return Arrays.asList( "reset" );
        }
        if( args.length == 2 && args[0].equals( "reset" ) )
        {
            var players = Bukkit.getOnlinePlayers();
            List<String> playerNames = new ArrayList<>();
            for( final var player : players )
            {
                playerNames.add( player.getName() );
            }
            return playerNames;
        }
        return new ArrayList<>();
    }
}