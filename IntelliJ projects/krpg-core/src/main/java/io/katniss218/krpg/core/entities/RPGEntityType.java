package io.katniss218.krpg.core.entities;

/**
 * Controls several things about the entity. The way in which their name is displayed, death announced,
 * and whether or not the drops are dropped onto the ground, or directly into the killer's inventory.
 */
public enum RPGEntityType
{
    /**
     * Normal entities.
     */
    NORMAL,
    MINI_BOSS,
    /**
     * Bosses have a bossbar, announce their spawn/death in global chat, and their drops are added directly into the killer's inventory.
     */
    BOSS
}
