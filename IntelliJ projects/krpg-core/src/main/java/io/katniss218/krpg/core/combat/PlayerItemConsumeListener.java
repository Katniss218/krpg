package io.katniss218.krpg.core.combat;

import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.items.RPGItemData;
import io.katniss218.krpg.core.loottables.LootTableDropper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerItemConsumeListener implements Listener
{
    public void Consume( RPGItemDef def, RPGItemData data, Player player )
    {
        if( def.food != null )
        {
            double totalFoodRestored = def.food;

            player.setFoodLevel( player.getFoodLevel() + (int)Math.floor( totalFoodRestored) );
            player.setSaturation( player.getSaturation() + (float)totalFoodRestored );
        }
        if( def.potionEffects != null )
        {
            for( final var potion : def.potionEffects )
            {
                if( LootTableDropper.randomRange( 0.0, 1.0 ) > potion.getChance() )
                    continue;

                player.addPotionEffect( new PotionEffect( potion.getEffectType(), potion.getDuration(), potion.getAmplifier(), false, true ) );
            }
        }
    }

    @EventHandler
    public void onConsume( PlayerItemConsumeEvent event )
    {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        RPGItemData data = RPGItemData.getFrom( item );
        if( data != null )
        {
            RPGItemDef def = RPGItemRegistry.get( data.getID() );
            if( def != null )
            {
                Consume( def, data, player );
            }
        }
    }
}
