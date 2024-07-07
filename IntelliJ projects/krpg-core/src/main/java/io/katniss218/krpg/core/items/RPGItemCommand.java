package io.katniss218.krpg.core.items;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
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
        if( args.length >= 1 && args[0].equals( "reload" ) )
        {
            if( !sender.hasPermission( "rpg." + command.getName() + ".reload" ))
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }
            RPGItemRegistry.reload();
            return true;
        }
        if( args.length >= 1 && args[0].equals( "sync" ) )
        {
            if( !sender.hasPermission( "rpg." + command.getName() + ".sync" ))
            {
                sender.sendMessage( KRPGCore.getNoPermissionMsg() );
                return true;
            }
            if( sender instanceof Player player )
            {
                var inv = player.getInventory();
                RPGItemFactory.syncInventory( inv, SyncContext.INVENTORY );
            }
            return true;
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
                if( args.length == 2 || args.length == 3 )
                {
                    String id = args[1];
                    int amount = 1;
                    if( args.length == 3 )
                    {
                        try
                        {
                            amount = Integer.parseInt( args[2] );
                        }
                        catch( Exception ex )
                        {
                        }
                    }
                    var def = RPGItemRegistry.get( id );
                    if( def != null )
                    {
                        var item = RPGItemFactory.createItemStack( def, amount, null, SyncContext.INVENTORY );
                        player.getInventory().addItem( item );
                    }
                }
            }
            return true;
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
        if( args.length == 2 && args[0].equals( "get" ) )
        {
            return RPGItemRegistry.getRegisteredIDs();
        }
        return new ArrayList<>();
    }
}