package io.katniss218.krpg.core.items;

import java.util.UUID;

public enum RPGItemType
{
    UNDEFINED( 0 ),
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

    public UUID getSlotUUID( int addend )
    {
        return new UUID( slotHash, 17 + addend );
    }

    /**
     * Checks if the slot is eligible to increase the stats of the player.
     * @param slot
     * @param isPrimary Whether or not the stat being checked is primary or additional.
     * @return
     */
    public boolean IsValid( EquipmentSlot slot, boolean isPrimary )
    {
        if( this == RPGItemType.ARMOR )
        {
            return slot == EquipmentSlot.HEAD
                    || slot == EquipmentSlot.CHEST
                    || slot == EquipmentSlot.LEGS
                    || slot == EquipmentSlot.FEET;
        }
        if( this == RPGItemType.WEAPON )
        {
            return slot == EquipmentSlot.MAIN_HAND
                    || slot == EquipmentSlot.OFF_HAND;
        }
        return false;
    }
}
