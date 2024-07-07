package io.katniss218.krpg.core.items;

import io.katniss218.krpg.core.shops.ShopInventory;
import io.katniss218.krpg.core.utils.ColorUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class PlayerJoinListener implements Listener
{
    @EventHandler
    public void onJoin( PlayerJoinEvent event )
    {
        Player player = event.getPlayer();

        PlayerInventory playerInventory = player.getInventory();
        RPGItemFactory.syncInventory( playerInventory, SyncContext.INVENTORY );
    }
}