package io.katniss218.krpg.core.items;

import java.util.UUID;

public enum RPGItemType
{
    WEAPON( 32768 ),
    ARMOR( 32769 ),
    CONSUMABLE( 32770 ),
    LOOT( 32771 );

    private final long slotHash;

    RPGItemType( long slotHash )
    {
        this.slotHash = slotHash;
    }

    public UUID getSlotUUID()
    {
        return new UUID( slotHash, 17 );
    }
}
