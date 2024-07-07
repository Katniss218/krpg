package io.katniss218.krpg.core.shops;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.definitions.RPGShopDef;
import io.katniss218.krpg.core.items.RPGItemFactory;
import io.katniss218.krpg.core.items.SyncContext;
import io.katniss218.krpg.core.utils.ColorUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnull;

public class ShopInventory implements InventoryHolder
{
    private final RPGShopDef def;
    private final Inventory inventory;

    public ShopInventory( RPGShopDef def )
    {
        // Create an Inventory with 9 slots, `this` here is our InventoryHolder.
        this.def = def;
        this.inventory = KRPGCore.getPlugin().getServer()
                .createInventory( this, 5 * 9, ColorUtils.GetComponent( "Shop: " + def.displayName ) );

        for( final var itemId : def.items )
        {
            final RPGItemDef itemDef = RPGItemRegistry.get( itemId );
            if( itemDef == null )
            {
                KRPGCore.getPluginLogger().warning( "Shop '" + def.id + "' uses unknown item id '" + itemId + "'." );
                continue;
            }

            var itemStack = RPGItemFactory.createItemStack( itemDef, 1, null, SyncContext.SHOP_BUY );
            inventory.addItem( itemStack );
        }
    }

    @Override
    @Nonnull
    public Inventory getInventory()
    {
        return this.inventory;
    }
}