package io.katniss218.krpg.core.shops;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGShopDef;
import io.katniss218.krpg.core.definitions.RPGShopRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

public class PlayerInteractEntityListener implements Listener
{
    @EventHandler
    public void onEntityClicked( PlayerInteractEntityEvent event )
    {
        Entity entity = event.getRightClicked();

        if( entity instanceof Villager )
        {
            RPGShopDef def = RPGShopRegistry.get( "default" );
            if( def != null )
            {
                ShopOpener.openShop( event.getPlayer(), def );
            }
            event.setCancelled( true );
        }
    }
}