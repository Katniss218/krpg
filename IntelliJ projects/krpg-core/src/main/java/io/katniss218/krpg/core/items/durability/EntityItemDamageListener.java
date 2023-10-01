package io.katniss218.krpg.core.items.durability;

import io.papermc.paper.event.entity.EntityDamageItemEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class EntityItemDamageListener implements Listener
{
    @EventHandler
    public void onPlayerItemDamage( EntityDamageItemEvent event )
    {
        Bukkit.broadcast( Component.text("(entity) item was damaged: " + event.getItem().displayName() ) );
    }
}
