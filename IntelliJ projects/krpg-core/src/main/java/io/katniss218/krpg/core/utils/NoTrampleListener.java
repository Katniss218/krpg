package io.katniss218.krpg.core.utils;

import io.katniss218.krpg.core.definitions.RPGShopDef;
import io.katniss218.krpg.core.definitions.RPGShopRegistry;
import io.katniss218.krpg.core.shops.ShopOpener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoTrampleListener implements Listener
{
    @EventHandler
    public void onInteract( EntityInteractEvent event )
    {
        if( event.getBlock().getType() == Material.FARMLAND )
        {
            event.setCancelled( true );
        }
    }
}