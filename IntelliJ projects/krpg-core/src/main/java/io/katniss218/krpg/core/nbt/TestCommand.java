package io.katniss218.krpg.core.nbt;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.List;

public class TestCommand implements CommandExecutor
{
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        // args start at the actual arguments, not the command name.

        if( sender instanceof Player )
        {
            Player player = (Player)sender;
            // Command logic for players
            var item = player.getInventory().getItemInMainHand();

            if( args[0].equals( "get" ) )
            {
                player.sendMessage( "get" );
                String id = ItemNBT.getItemID( item );
                if( id != null )
                {
                    player.sendMessage( id );
                }
            }
            if( args[0].equals( "set" ) )
            {
                var item2 = ItemNBT.setItemID( item, args[1] );
                player.getInventory().setItemInMainHand( item2 );
            }

            if( args[0].equals( "getnearestzombie" ) )
            {
                Location loc = player.getLocation();
                List<Entity> entities = player.getWorld().getEntities();
                Entity nearest = null;
                double nearestDistanceSquared = Double.MAX_VALUE;
                for( var entity : entities )
                {
                    if( entity.getType() != EntityType.ZOMBIE )
                    {
                        continue;
                    }
                    double dstSq = loc.distanceSquared( entity.getLocation() );
                    if( dstSq < nearestDistanceSquared )
                    {
                        nearest = entity;
                        nearestDistanceSquared = dstSq;
                    }
                }

                String id = EntityNBT.getEntityID( nearest );
                if( id != null )
                {
                    player.sendMessage( id );
                }
            }

            if( args[0].equals( "setnearestzombie" ) )
            {
                Location loc = player.getLocation();
                List<Entity> entities = player.getWorld().getEntities();
                Entity nearest = null;
                double nearestDistanceSquared = Double.MAX_VALUE;
                for( var entity : entities )
                {
                    if( entity.getType() != EntityType.ZOMBIE )
                    {
                        continue;
                    }
                    double dstSq = loc.distanceSquared( entity.getLocation() );
                    if( dstSq < nearestDistanceSquared )
                    {
                        nearest = entity;
                        nearestDistanceSquared = dstSq;
                    }
                }

                EntityNBT.setEntityID( nearest, args[1] );
            }

            player.sendMessage( "You executed the command!" );
        }

        // Your command logic goes here
        return true; // Return true if the command was handled successfully
    }
}