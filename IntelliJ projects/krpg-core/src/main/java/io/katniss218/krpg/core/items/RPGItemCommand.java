package io.katniss218.krpg.core.items;

import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RPGItemCommand implements TabExecutor
{

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args )
    {
        if( args.length == 1 && args[0].equals( "sync" ) )
        {
            if( sender instanceof Player player )
            {
                var inv = player.getInventory();
                inv.setItemInMainHand( RPGItemUtils.syncItem( inv.getItemInMainHand() ) );
            }
        }
        if( args.length == 1 && args[0].equals( "get" ) )
        {
            if( sender instanceof Player player )
            {
                for( var id : RPGItemRegistry.getRegisteredIDs() )
                {
                    player.sendMessage( id );
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
            return Arrays.asList( "get", "sync" );
        }
        if( args.length <= 2 && args[1].equals( "get" ) )
        {
            return RPGItemRegistry.getRegisteredIDs();
        }
        return null;
    }
}