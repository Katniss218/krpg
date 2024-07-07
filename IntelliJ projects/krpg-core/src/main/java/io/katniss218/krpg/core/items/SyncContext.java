package io.katniss218.krpg.core.items;

public enum SyncContext
{
    /**
     * Sync the item as an item that the player sees in their inventory.
     */
    INVENTORY,
    /**
     * Sync the item as an item that the player sees in a shop inventory (to be bought).
     */
    SHOP_BUY,
    /**
     * Sync the item as an item that the player sees in a shop inventory (to be sold).
     */
    SHOP_SELL
}
