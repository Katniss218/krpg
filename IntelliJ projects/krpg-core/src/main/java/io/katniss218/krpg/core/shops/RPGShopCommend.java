package io.katniss218.krpg.core.shops;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGLootTableRegistry;
import io.katniss218.krpg.core.definitions.RPGShopDef;
import io.katniss218.krpg.core.definitions.RPGShopRegistry;
import io.katniss218.krpg.core.loottables.LootTableDropper;
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

public class RPGShopCommend implements TabExecutor
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
            RPGLootTableRegistry.reload();
            sender.sendMessage( ColorUtils.GetComponent( "&aReloaded all loot tables." ) );
        }
        if( args.length >= 1 && args[0].equals( "open" ) )
        {
            if( !sender.hasPermission( "rpg." + command.getName() + ".open" ) )
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }

            if( args.length == 2 && sender instanceof Player player )
            {
                String id = args[1];
                RPGShopDef def = RPGShopRegistry.get( id );
                if( def != null )
                {
                    ShopOpener.openShop( player, def );
                    player.sendMessage( ColorUtils.GetComponent( "&aOpened shop '" + id + "'." ) );
                }
                return true;
            }
            if( args.length == 3 )
            {
                String id = args[1];
                Player target = Bukkit.getPlayer( args[2] );
                if( target != null )
                {
                    RPGShopDef def = RPGShopRegistry.get( id );
                    if( def != null )
                    {
                        ShopOpener.openShop( target, def );
                        target.sendMessage( ColorUtils.GetComponent( "&aOpened shop '" + id + "' to player " + target.getName() + "." ) );
                    }
                }
                return true;
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
            return Arrays.asList( "open" );
        }
        if( args.length == 2 && args[0].equals( "open" ) )
        {
            return RPGShopRegistry.getRegisteredIDs();
        }
        if( args.length == 3 && args[0].equals( "open" ) )
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