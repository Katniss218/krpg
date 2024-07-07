package io.katniss218.krpg.core.loottables;

import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.definitions.RPGLootTableDef;
import io.katniss218.krpg.core.items.RPGItemFactory;
import io.katniss218.krpg.core.items.SyncContext;
import io.katniss218.krpg.core.levels.LevelUtils;
import io.katniss218.krpg.core.money.MoneyUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LootTableDropper
{
    public static int randomRange( int min, int max )
    {
        return (int)((Math.random() * (max - min)) + min);
    }

    public static double randomRange( double min, double max )
    {
        return (Math.random() * (max - min)) + min;
    }

    public static void DropLootTable( @Nonnull RPGLootTableDef def, @Nullable Location location, @Nullable Player player )
    {
        if( location != null )
        {
            if( def.items != null )
            {
                for( final var item : def.items )
                {
                    if( randomRange( 0.0, 1.0 ) > item.chance )
                        continue;

                    for( final var itemId : item.itemIds )
                    {
                        RPGItemDef itemDef = RPGItemRegistry.get( itemId );

                        if( itemDef == null )
                            continue;

                        int countRemaining = randomRange( item.min, item.max + 1 );
                        while( countRemaining > 0 )
                        {
                            var countToAdd = Math.min( countRemaining, itemDef.baseItem.getMaxStackSize() );

                            var itemstack = RPGItemFactory.createItemStack( itemDef, countToAdd, null, SyncContext.INVENTORY );
                            location.getWorld().dropItem( location, itemstack );
                            countRemaining -= countToAdd;
                        }
                    }
                }
            }
        }
        if( player != null )
        {
            if( def.xp != null )
            {
                LevelUtils.addXp( player, randomRange( def.xp.min, def.xp.max ) );
            }
            if( def.money != null )
            {
                MoneyUtils.addMoney( player, randomRange( def.money.min, def.money.max ) );
            }
        }
    }

    public static void GiveLootTable( @Nonnull RPGLootTableDef def, @Nonnull Player player )
    {
        if( def.items != null )
        {
            for( final var item : def.items )
            {
                if( randomRange( 0.0, 1.0 ) > item.chance )
                    continue;

                for( final var itemId : item.itemIds )
                {
                    RPGItemDef itemDef = RPGItemRegistry.get( itemId );

                    if( itemDef == null )
                        continue;

                    int countRemaining = randomRange( item.min, item.max + 1 );
                    while( countRemaining > 0 )
                    {
                        var countToAdd = Math.min( countRemaining, itemDef.baseItem.getMaxStackSize() );

                        var itemstack = RPGItemFactory.createItemStack( itemDef, countToAdd, null, SyncContext.INVENTORY );
                        player.getInventory().addItem( itemstack );
                        countRemaining -= countToAdd;
                    }
                }
            }
        }

        if( def.xp != null )
        {
            LevelUtils.addXp( player, randomRange( def.xp.min, def.xp.max ) );
        }
        if( def.money != null )
        {
            MoneyUtils.addMoney( player, randomRange( def.money.min, def.money.max ) );
        }
    }
}