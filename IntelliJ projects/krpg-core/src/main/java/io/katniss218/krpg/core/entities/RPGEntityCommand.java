package io.katniss218.krpg.core.entities;

import io.katniss218.krpg.core.definitions.RPGEntityRegistry;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.items.RPGItemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RPGEntityCommand implements TabExecutor
{

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args )
    {
        if( args.length >= 1 && args[0].equals( "spawn" ) )
        {
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
                    var def = RPGEntityRegistry.get( id );
                    if( def != null )
                    {
                        var item = RPGEntityUtils.createEntity( def, player.getLocation(), amount, null );
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
            return Arrays.asList( "spawn" );
        }
        if( args.length == 2 && args[0].equals( "spawn" ) )
        {
            return RPGEntityRegistry.getRegisteredIDs();
        }
        return new ArrayList<>();
    }
}