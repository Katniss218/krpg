package io.katniss218.krpg.core.items.durability;

import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.items.RPGItemData;
import io.katniss218.krpg.core.items.RPGItemFactory;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerItemDamageListener implements Listener
{
    private void DamageItem( PlayerItemDamageEvent event, ItemStack item, RPGItemData data )
    {
        event.setCancelled( true );

        data.durabilityRemaining--;

        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();

        if( data.durabilityRemaining <= 0 )
            inv.removeItem( item );
        else
        {
            // sync
            var item2 = RPGItemFactory.syncItemStack( item, data );

            if( inv.getItemInMainHand() == item )
                inv.setItemInMainHand( item2 );
            if( inv.getItemInOffHand() == item )
                inv.setItemInOffHand( item2 );

            if( inv.getHelmet() == item )
                inv.setHelmet( item2 );
            if( inv.getChestplate() == item )
                inv.setChestplate( item2 );
            if( inv.getLeggings() == item )
                inv.setLeggings( item2 );
            if( inv.getBoots() == item )
                inv.setBoots( item2 );
        }
    }

    @EventHandler
    public void onPlayerItemDamage( PlayerItemDamageEvent event )
    {
        var item = event.getItem();
        RPGItemData data = RPGItemData.getFrom( item );
        if( data == null )
            return;

        DamageItem( event, item, data );
    }
}