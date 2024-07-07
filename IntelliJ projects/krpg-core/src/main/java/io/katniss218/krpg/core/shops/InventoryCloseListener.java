package io.katniss218.krpg.core.shops;

import io.katniss218.krpg.core.items.RPGItemFactory;
import io.katniss218.krpg.core.items.SyncContext;
import io.katniss218.krpg.core.utils.ColorUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryCloseListener implements Listener
{
    @EventHandler
    public void onInventoryClose( InventoryCloseEvent event )
    {
        Inventory inventory = event.getInventory();

        HumanEntity player = event.getPlayer();

        if( inventory.getHolder( false ) instanceof ShopInventory shopInventory )
        {
            player.sendMessage( ColorUtils.GetComponent( "&aThanks for shopping!" ) );
        }

        PlayerInventory playerInventory = player.getInventory();
        RPGItemFactory.syncInventory( playerInventory, SyncContext.INVENTORY );
    }
}