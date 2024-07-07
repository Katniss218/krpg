package io.katniss218.krpg.core.items.durability;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.items.RPGItemData;
import io.katniss218.krpg.core.items.RPGItemFactory;
import io.katniss218.krpg.core.items.SyncContext;
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
            var item2 = RPGItemFactory.syncItemStack( item, data, SyncContext.INVENTORY );

            if( inv.getItemInMainHand().equals( item ) )
            {
                inv.setItemInMainHand( item2 );
            }
            if( inv.getItemInOffHand().equals( item ) )
            {
                inv.setItemInOffHand( item2 );
            }

            var helmet = inv.getHelmet();
            if( helmet != null && helmet.equals( item ) )
                inv.setHelmet( item2 );
            var chestplate = inv.getChestplate();
            if( chestplate != null && chestplate.equals( item ) )
                inv.setChestplate( item2 );
            var leggings = inv.getLeggings();
            if( leggings != null && leggings.equals( item ) )
                inv.setLeggings( item2 );
            var boots = inv.getBoots();
            if( boots != null && boots.equals( item ) )
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