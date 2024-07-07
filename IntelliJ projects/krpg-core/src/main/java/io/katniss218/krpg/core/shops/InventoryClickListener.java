package io.katniss218.krpg.core.shops;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.definitions.RPGItemDef;
import io.katniss218.krpg.core.definitions.RPGItemRegistry;
import io.katniss218.krpg.core.items.RPGItemData;
import io.katniss218.krpg.core.items.RPGItemFactory;
import io.katniss218.krpg.core.items.SyncContext;
import io.katniss218.krpg.core.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener
{
    @EventHandler
    public void onInventoryClick( InventoryClickEvent event )
    {
        Inventory inventory = event.getInventory();
        if( !(inventory.getHolder( false ) instanceof ShopInventory shopInventory) )
        {
            return;
        }

        event.setCancelled( true );

        if( event.getClick() != ClickType.LEFT )
        {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if( clickedItem == null )
        {
            return;
        }

        RPGItemData rpgItem = RPGItemData.getFrom( clickedItem );
        if( rpgItem == null )
        {
            return;
        }

        RPGItemDef itemDef = RPGItemRegistry.get( rpgItem.getID() );
        if( itemDef == null )
        {
            return;
        }

        HumanEntity clicker = event.getWhoClicked();

        if( !(clicker instanceof Player player) )
        {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();
        Inventory playerInventory = player.getInventory();

        int itemCount = clickedItem.getAmount();
        double stackValue = itemDef.value * itemCount;

        if( clickedInventory != playerInventory )
        {
            // if item is in the shopInventory - try to buy it.

            var vaultEco = KRPGCore.getVaultEconomy();
            if( vaultEco.getBalance( player ) < stackValue )
            {
                player.sendMessage( ColorUtils.GetComponent( "&cNot enough money to buy '&6" + itemDef.displayName + "&c'." ) );
                return;
            }

            ItemStack newItem = RPGItemFactory.createItemStack( itemDef, itemCount, rpgItem, SyncContext.SHOP_SELL );
            playerInventory.addItem( newItem );
            vaultEco.withdrawPlayer( player, stackValue );
            player.sendMessage( ColorUtils.GetComponent( "&aBought " + itemCount + "x " + itemDef.displayName + " for ₡" + stackValue ) );
        }
        else
        {
            // if item is in the player's inventory - try to sell it.

            var vaultEco = KRPGCore.getVaultEconomy();
            stackValue /= 5.0;

            playerInventory.setItem( event.getSlot(), new ItemStack( Material.AIR ) );
            vaultEco.depositPlayer( player, stackValue );
            player.sendMessage( ColorUtils.GetComponent( "&aSold " + itemCount + "x " + itemDef.displayName + " for ₡" + stackValue ) );
        }
    }
}