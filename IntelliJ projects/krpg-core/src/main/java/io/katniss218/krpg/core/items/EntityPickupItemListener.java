package io.katniss218.krpg.core.items;

import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EntityPickupItemListener implements Listener
{
    @EventHandler
    public void onItemPickup( EntityPickupItemEvent event )
    {
        var entity = event.getEntity();
        if( !(entity instanceof Player player) )
        {
            return;
        }

        ItemStack itemStack = event.getItem().getItemStack();
        RPGItemData rpgItem = RPGItemData.getFrom( itemStack );
        if( rpgItem == null )
        {
            return;
        }

        RPGItemDef itemDef = RPGItemRegistry.get( rpgItem.getID() );
        if( itemDef == null )
        {
            return;
        }

        // handle money - pick up and add to balance.
        /*if( )

        PlayerInventory playerInventory = player.getInventory();
        RPGItemFactory.syncInventory( playerInventory, SyncContext.INVENTORY );*/
    }
}