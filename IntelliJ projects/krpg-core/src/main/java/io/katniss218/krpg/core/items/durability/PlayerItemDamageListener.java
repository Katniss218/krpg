package io.katniss218.krpg.core.items.durability;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class PlayerItemDamageListener implements Listener
{
    @EventHandler
    public void onPlayerItemDamage( PlayerItemDamageEvent event )
    {
        Bukkit.broadcast( Component.text("(player) item was damaged: " + event.getItem().displayName() ) );
    }
}
