package io.katniss218.krpg.core.loottables;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.definitions.RPGLootTableDef;
import io.katniss218.krpg.core.definitions.RPGLootTableRegistry;
import io.katniss218.krpg.core.entities.RPGEntityFactory;
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

public class RPGLootTableCommand implements TabExecutor
{
    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args )
    {
        if( args.length >= 1 && args[0].equals( "reload" ) )
        {
            if( !sender.hasPermission( "rpg." + command.getName() + ".reload" ))
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }
            RPGLootTableRegistry.Reload();
            sender.sendMessage( ColorUtils.GetComponent( "&aReloaded all loot tables." ) );
        }
        if( args.length >= 1 && args[0].equals( "get" ) )
        {
            if( !sender.hasPermission( "rpg." + command.getName() + ".get" ))
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }
            if( sender instanceof Player player )
            {
                if( args.length == 2 )
                {
                    String id = args[1];
                    var def = RPGLootTableRegistry.get( id );
                    if( def != null )
                    {
                        LootTableDropper.GiveLootTable( def, player );
                        player.sendMessage( ColorUtils.GetComponent( "&aGave loot table '" + id + "' to player " + player.getName() + "." ) );
                    }
                }
            }
        }
        if( args.length >= 1 && args[0].equals( "give" ) )
        {
            if( !sender.hasPermission( "rpg." + command.getName() + ".give" ))
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }
            if( sender instanceof Player player )
            {
                if( args.length == 3 )
                {
                    String id = args[1];
                    Player target = Bukkit.getPlayer( args[2] );
                    if( target != null )
                    {
                        var def = RPGLootTableRegistry.get( id );
                        if( def != null )
                        {
                            LootTableDropper.GiveLootTable( def, target );
                            player.sendMessage( ColorUtils.GetComponent( "&aGave loot table '" + id + "' to player " + player.getName() + "." ) );
                        }
                    }
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
            // completing first arg.
            return Arrays.asList( "get", "give" );
        }
        if( args.length == 2 && args[0].equals( "get" ) )
        {
            return RPGLootTableRegistry.getRegisteredIDs();
        }
        if( args.length == 2 && args[0].equals( "give" ) )
        {
            var players = Bukkit.getOnlinePlayers();
            List<String> playerNames = new ArrayList<>();
            for( final var player : players )
            {
                playerNames.add( player.getName() );
            }
            return playerNames;
        }
        if( args.length == 3 && args[0].equals( "give" ) )
        {
            return RPGLootTableRegistry.getRegisteredIDs();
        }
        return new ArrayList<>();
    }
}