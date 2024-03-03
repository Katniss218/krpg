package io.katniss218.krpg.core.combat;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener
{
    @EventHandler
    public void onEntityDamage( EntityDamageEvent event )
    {
        //Bukkit.broadcast( Component.text("damaged entity: " + event.getEntity().getName() ) );
        //var cause = event.getCause();
    }
}
