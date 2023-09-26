package io.katniss218.krpg.core.nbt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor
{
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        if( sender instanceof Player )
        {
            Player player = (Player)sender;
            // Command logic for players
            var item = player.getInventory().getItemInMainHand();


            player.sendMessage( args );

            if( args[0] == "get" )
            {
                String id = ItemNBT.getItemID( item );
                if( id != null )
                {
                    player.sendMessage( id );
                }
            }
            if( args[0] == "set" )
            {
                ItemNBT.setItemID( item, args[1] );
            }

            player.sendMessage( "You executed the command!" );
        }

        // Your command logic goes here
        return true; // Return true if the command was handled successfully
    }
}