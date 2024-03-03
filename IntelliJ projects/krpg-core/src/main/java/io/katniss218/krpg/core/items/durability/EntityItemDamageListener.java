package io.katniss218.krpg.core.items.durability;

import io.katniss218.krpg.core.items.RPGItemData;
import io.katniss218.krpg.core.items.RPGItemFactory;
import io.papermc.paper.event.entity.EntityDamageItemEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.PlayerInventory;

public class EntityItemDamageListener implements Listener
{
    @EventHandler
    public void onEntityItemDamage( EntityDamageItemEvent event )
    {
        // Nothing yet.
    }
}
