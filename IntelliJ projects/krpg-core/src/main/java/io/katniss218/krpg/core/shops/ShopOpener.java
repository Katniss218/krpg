package io.katniss218.krpg.core.shops;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGShopDef;
import io.katniss218.krpg.core.definitions.RPGShopRegistry;
import io.katniss218.krpg.core.items.RPGItemFactory;
import io.katniss218.krpg.core.items.SyncContext;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ShopOpener
{
    @Nullable
    public static ShopInventory openShop( @Nonnull Player player, @Nonnull RPGShopDef shopDef )
    {
        ShopInventory shopInventory = new ShopInventory( shopDef );

        var inv = shopInventory.getInventory();
        RPGItemFactory.syncInventory( inv, SyncContext.SHOP_BUY );

        player.openInventory( inv );
        RPGItemFactory.syncInventory( player.getInventory(), SyncContext.SHOP_SELL );

        return shopInventory;
    }
}