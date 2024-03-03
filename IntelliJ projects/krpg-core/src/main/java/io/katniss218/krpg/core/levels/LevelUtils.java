package io.katniss218.krpg.core.levels;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;

public class LevelUtils
{
    final static double xpLevelRatio = 1.25;

    /**
     * Calculates the xp required to level up to the specified level, from the level below.
     * @param level The target level.
     * @return The xp points required to level up from (level-1) to level.
     */
    public static double getXpToLevel( int level )
    {
        return Math.floor( 20 * Math.pow( xpLevelRatio, level-1) );
    }

    /**
     * Calculates the xp required to level up to the specified level, from the specified starting level.
     * @param level The target level.
     * @param startingLevel The starting level.
     * @return The xp points required to level up or down from (startingLevel) to level.
     */
    public static double getXpToLevelFrom( int level, int startingLevel )
    {
        if( level < 0 || startingLevel < 0 )
        {
            throw new IllegalArgumentException("Level and startingLevel must be > 0");
        }

        double acc = 0.0;
        if( startingLevel < level )
        {
            for( ; startingLevel < level; startingLevel++ )
            {
                acc += Math.floor( 20 * Math.pow( xpLevelRatio, startingLevel-1) );
            }
            return acc;
        }
        if( startingLevel > level )
        {
            for( ; startingLevel > level; level-- )
            {
                acc += Math.floor( 20 * Math.pow( xpLevelRatio, level-1) );
            }
            return acc;
        }
        return 0;
    }

    public static double GetXp( @Nonnull Player player )
    {
        return 0;
    }

    public static int GetLevel( @Nonnull Player player )
    {
        return 0;
    }

    public static void AddXp( @Nonnull Player player, double xp )
    {

    }

    public static void SetLevel( @Nonnull Player player, int level )
    {

    }

    private static void SyncPlayer( @Nonnull Player player, double xp, int level )
    {
        player.setLevel( level );

        double nextLevelExp = player.getExpToLevel();

        double perc = 0.5;

        player.setExp( (float)((nextLevelExp * perc) / nextLevelExp) );
    }
}